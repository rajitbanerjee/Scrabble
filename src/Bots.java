import javafx.application.Application;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Bots {

    private static final String[] ALL_BOT_NAMES = {"Bot0","Bot1"};
    private BotAPI[] bots = new BotAPI[Scrabble.NUM_PLAYERS];

    Bots(Scrabble scrabble, UserInterface ui, Application.Parameters parameters) {
        List<String> params = parameters.getRaw();
        String[] botNames = new String[Scrabble.NUM_PLAYERS];
//        if (params.size() < Scrabble.NUM_PLAYERS) {
            botNames[0] = "Bot0";
            botNames[1] = "Bot1";
//        } else {
//            for (int i = 0; i < Scrabble.NUM_PLAYERS; i++) {
//                boolean found = false;
//                for (int j = 0; (j < ALL_BOT_NAMES.length) && !found; j++) {
//                    if (params.get(i).equals(ALL_BOT_NAMES[j])) {
//                        found = true;
//                        botNames[i] = params.get(i);
//                    }
//                }
//                if (!found) {
//                    System.out.println("Error: Bot name not found");
//                    System.exit(-1);
//                }
//            }
//        }
        for (int i=0; i<Scrabble.NUM_PLAYERS; i++) {
            try {
                Class<?> botClass = Class.forName(botNames[i]);
                Constructor<?> botCons = botClass.getDeclaredConstructor(PlayerAPI.class, OpponentAPI.class, BoardAPI.class, UserInterfaceAPI.class, DictionaryAPI.class);
                if (i==0) {
                    bots[i] = (BotAPI) botCons.newInstance(scrabble.getCurrentPlayer(), scrabble.getOpposingPlayer(), scrabble.getBoard(), ui, scrabble.getDictionary());
                } else {
                    bots[i] = (BotAPI) botCons.newInstance(scrabble.getOpposingPlayer(), scrabble.getCurrentPlayer(), scrabble.getBoard(), ui, scrabble.getDictionary());
                }
            } catch (IllegalAccessException ex) {
                System.out.println("Error: Bot instantiation fail (IAE)");
                Thread.currentThread().interrupt();
            } catch (InstantiationException ex) {
                System.out.println("Error: Bot instantiation fail (IE)");
                Thread.currentThread().interrupt();
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: Bot instantiation fail (CNFE)");
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException ex) {
                System.out.println("Error: Bot instantiation fail (ITE)");
                Thread.currentThread().interrupt();
            } catch (NoSuchMethodException ex) {
                System.out.println("Error: Bot instantiation fail (NSME)");
                Thread.currentThread().interrupt();
            }
        }
    }

    public BotAPI getBot(int index) {
        return bots[index];
    }
}
