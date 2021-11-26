package falcon.jacob.pandemichelper.PandemicGame;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;

public class EventCard extends PlayerCard {

    public static final String ONE_QUIET_NIGHT_EVENT = "One Quiet Night";
    public static final String FORECAST_EVENT = "Forecast";
    public static final String RESILIENT_POPULATION_EVENT = "Resilient Population";
    public static final String AIRLIFT_EVENT = "Airlift";
    public static final String GOVERNMENT_GRANT_EVENT = "Government Grant";

    public EventCard(String name){
        this.name = name;
        this.type = CardType.EVENT;
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub

    }

    @Override
    public PlayerCard deepCopy() {
        return new EventCard(this.name);
    }
}