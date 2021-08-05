package s05.p12a104.mafia.stomp.response;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import s05.p12a104.mafia.domain.entity.GameSession;
import s05.p12a104.mafia.domain.entity.Player;
import s05.p12a104.mafia.domain.entity.StompMessageType;

@Getter
@RequiredArgsConstructor
public class GameSessionStompJoinRes {

  private final StompMessageType type = StompMessageType.JOIN;
  private final String hostId;
  private final Map<String, Player> playerMap;

  public static GameSessionStompJoinRes of(GameSession gameSession) {
    return new GameSessionStompJoinRes(gameSession.getHostId(), gameSession.getPlayerMap());
  }
}
