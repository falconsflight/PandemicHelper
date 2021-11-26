package falcon.jacob.pandemichelper.PandemicGame;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;
import falcon.jacob.pandemichelper.PandemicGame.Interface.ICard;

public class InfectionCard implements ICard {

    protected String name;

    public InfectionCard(String name){
        this.name = name;
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CardType getType() {
        return CardType.INFECTION;
    }

    public InfectionCard deepCopy() {
        return new InfectionCard(this.name);
    }

    @Override
    public String toString(){
        return this.name;
    }
}