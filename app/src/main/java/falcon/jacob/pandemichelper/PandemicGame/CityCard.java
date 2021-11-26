package falcon.jacob.pandemichelper.PandemicGame;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardColor;
import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;

public class CityCard extends PlayerCard {

    protected CardColor color;
    protected long population;

    public CityCard(String name, CardColor color, long population){
        this.name = name;
        this.color = color;
        this.population = population;
        this.type = CardType.CITY;
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub
    }

    public CardColor getColor(){
        return this.color;
    }

    public long getPopulation() {
        return population;
    }
    
    @Override
    public CityCard deepCopy(){
        return new CityCard(this.name, this.color, this.population);
    }
}