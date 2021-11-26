package falcon.jacob.pandemichelper.PandemicGame;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;

public class EpidemicCard extends PlayerCard {

    public EpidemicCard(){
        this.name = "EPIDEMIC";
        this.type = CardType.EPIDEMIC;
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub

    }

    @Override
    public PlayerCard deepCopy() {
        return new EpidemicCard();
    }
    
}