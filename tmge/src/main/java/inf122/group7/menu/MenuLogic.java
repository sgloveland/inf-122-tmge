package inf122.group7.menu;

import inf122.group7.model.GameLogic;
import inf122.group7.reflections.ReflectionLogic;

public class MenuLogic {
    private String[] gameNames;
    private GameLogic[] gameLogics;

    public MenuLogic() throws Exception {
        // use reflections to automatically search for the different GameLogics
        gameLogics = ReflectionLogic.getInstance().getAllGameLogics();

        gameNames = new String[gameLogics.length];
        for (int i = 0; i < gameLogics.length; i++) {
            gameNames[i] = gameLogics[i].getGameName();
        }
    }

    public int getNumGames() {
        return gameLogics.length;
    }

    public String getGameName(int gameIndex) {
        return gameNames[gameIndex];
    }

    public GameLogic getGameLogic(int gameIndex) {
        return gameLogics[gameIndex];
    }
}
