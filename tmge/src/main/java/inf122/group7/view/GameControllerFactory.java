package inf122.group7.view;

import inf122.group7.model.GameLogic;
import inf122.group7.reflections.ReflectionLogic;

public class GameControllerFactory {

    public GameController createGameLogicController(int playerIndex, GameLogic gameLogic) throws Exception {
        return ReflectionLogic.getInstance()
                .getMatchingGameControllerConstructor(gameLogic)
                .newInstance(playerIndex, gameLogic);
    }
}
