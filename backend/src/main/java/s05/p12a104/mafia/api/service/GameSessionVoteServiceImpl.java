package s05.p12a104.mafia.api.service;

import java.util.Timer;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import s05.p12a104.mafia.domain.entity.Vote;
import s05.p12a104.mafia.domain.enums.GamePhase;
import s05.p12a104.mafia.domain.repository.VoteRepository;
import s05.p12a104.mafia.redispubsub.RedisPublisher;
import s05.p12a104.mafia.stomp.task.DayDiscussionVoteFinTimerTask;
import s05.p12a104.mafia.stomp.task.StartFinTimerTask;
import s05.p12a104.mafia.vote.GameSessionVoteReq;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameSessionVoteServiceImpl implements GameSessionVoteService {

  private final RedisPublisher redisPublisher;
  private final VoteRepository voteRepository;
  private DayDiscussionVoteFinTimerTask task;
  private Timer timer;

  @Override
  public void startVote(String roomId) {
    timer = new Timer();
    task = new DayDiscussionVoteFinTimerTask(redisPublisher);
    task.setRoomId(roomId);
    timer.schedule(task, 10 * 1000);
  }

  @Override
  public void endVote(String roomId) {
    task.cancel();
  }

  @Override
  public Vote vote(String roomId, String playerId, GameSessionVoteReq req) {

    String voteId = roomId + req.getPhase().toString();

    voteRepository.vote(voteId, playerId, req.getVote());

    return voteRepository.findVoteById(voteId);
    // if (req.getPhase() == GamePhase.DAY_DISCUSSION) {
    //
    // } else if (req.getPhase() == GamePhase.DAY_DISCUSSION) {
    //
    // } else if (req.getPhase() == GamePhase.DAY_ELIMINATION) {
    //
    // } else if (req.getPhase() == GamePhase.NIGHT_VOTE) {
    //
    // } else {
    //
    // }

  }

  @Override
  public Vote getVote(String roomId, GameSessionVoteReq req) {
    String voteId = roomId + req.getPhase().toString();
    return voteRepository.findVoteById(voteId);
  }

  @Override
  public int confirmVote(String roomId, String playerId, GameSessionVoteReq req) {
    String voteId = roomId + req.getPhase().toString();

    return voteRepository.confirm(voteId, playerId);
  }

  @Override
  public void createVote(String roomId, GamePhase phase) {
    voteRepository.createVote(roomId, phase);
  }

  @Override
  public void finishVote(String roomId, GameSessionVoteReq req) {
    voteRepository.finishVote(roomId, req.getPhase());
  }

  @Override
  public void publishRedis(String roomId) {
    redisPublisher.publish(new ChannelTopic("VOTE_FIN"), roomId);
  }
}