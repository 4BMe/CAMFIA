package s05.p12a104.mafia.stomp.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import s05.p12a104.mafia.domain.entity.Player;
import s05.p12a104.mafia.domain.enums.Color;

@Getter
@RequiredArgsConstructor
public class StompExistingPlayer {

  private final String nickname;
  private final Color color;
  private final boolean alive;
  private final boolean suspicious;

  public static StompExistingPlayer of(Player player) {
    return new StompExistingPlayer(player.getNickname(), player.getColor(), player.isAlive(),
        player.isSuspicious());
  }
}
