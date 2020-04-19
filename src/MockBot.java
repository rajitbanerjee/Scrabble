public class MockBot implements BotAPI {

    MockBot(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
        // do nothing
    }

    @Override
    public String getCommand() {
        return "NAME MockBot";
    }
}
