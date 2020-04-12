public interface OpponentAPI {

    int getPrintableId();

    String getName();

    int getScore();

    @Override
    String toString();

}