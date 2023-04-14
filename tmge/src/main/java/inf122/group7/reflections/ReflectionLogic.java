package inf122.group7.reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import inf122.group7.GameDriver;
import inf122.group7.model.GameLogic;
import inf122.group7.view.GameController;

public class ReflectionLogic {
    private static ReflectionLogic instance = null;
    private Reflections reflections;

    // maps the package name of each controller to the associated controller
    // class
    private HashMap<String, Class<? extends GameController>> gameControllerMap;

    public static ReflectionLogic getInstance() {
        if (instance == null) {
            instance = new ReflectionLogic();
        }
        return instance;
    }

    private ReflectionLogic() {
        reflections = new Reflections(GameDriver.class.getPackageName());
        gameControllerMap = searchForGameControllers();
    }

    public GameLogic[] getAllGameLogics() throws Exception {
        Set<Class<? extends GameLogic>> gameLogicsSet = reflections.getSubTypesOf(GameLogic.class);
        GameLogic[] gameLogics = new GameLogic[gameLogicsSet.size()];

        int gameIndex = 0;
        for (Class<? extends GameLogic> gameLogicClass : gameLogicsSet) {
            GameLogic gameLogic = gameLogicClass.getDeclaredConstructor().newInstance();
            gameLogics[gameIndex++] = gameLogic;
        }
        return gameLogics;
    }

    public Constructor<? extends GameController> getMatchingGameControllerConstructor(GameLogic gameLogic)
            throws Exception {
        // find the constructor with the default parameter types
        Class<?>[] parameterTypes = GameController.class.getConstructors()[0].getParameterTypes();
        String packageName = gameLogic.getClass().getPackageName();

        if (!gameControllerMap.containsKey(packageName)) {
            // use default GameController if not implemented
            return GameController.class.getDeclaredConstructor(parameterTypes);
        }

        return gameControllerMap.get(packageName)
                .getDeclaredConstructor(parameterTypes);
    }

    private HashMap<String, Class<? extends GameController>> searchForGameControllers() {
        Set<Class<? extends GameController>> gameControllersSet = reflections
                .getSubTypesOf(GameController.class);
        HashMap<String, Class<? extends GameController>> gameControllers = new HashMap<>();

        for (Class<? extends GameController> gameControllerClass : gameControllersSet) {
            gameControllers.put(gameControllerClass.getPackageName(),
                    gameControllerClass);
        }
        return gameControllers;
    }
}
