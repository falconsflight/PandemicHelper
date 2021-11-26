package falcon.jacob.pandemichelper.PandemicGame;

import java.util.ArrayList;
import java.util.Collection;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import falcon.jacob.pandemichelper.PandemicGame.Interface.IDeck;
import falcon.jacob.pandemichelper.PandemicGame.Util.JsonFileReader;

public class InfectionDeck implements IDeck<InfectionCard> {

    private boolean printDebugInfo;
    protected ArrayList<InfectionCard> cards;

    public InfectionDeck() {
        this.cards = new ArrayList<>();
        this.printDebugInfo = false;
        this.load();
        this.shuffle();
    }

    public InfectionDeck(boolean printDebugInfo) {
        this.cards = new ArrayList<>();
        this.printDebugInfo = printDebugInfo;
        this.load();
        this.shuffle();
    }

    @Override
    public Collection<InfectionCard> getCards() {
        return this.deepCopy();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    @Override
    public InfectionCard draw() {
        return this.cards.remove(0);
    }

    public InfectionCard drawBottomCard(){
        return this.cards.remove(this.cards.size()-1);
    }

    public void addCardToDeck(InfectionCard card){
        this.cards.add(0, card);
    }

    public void load() {
       this.readInfectionCards();
    }

    private void readInfectionCards() {
        try {
            String filePath = "C:\\Users\\jefal\\AndroidStudioProjects\\PandemicHelper\\app\\src\\main\\java\\falcon\\jacob\\pandemichelper\\PandemicGame\\lib\\infectionCards.json";
            JsonFileReader jsonFileReader = new JsonFileReader();
            convertJSONObjectToCards(jsonFileReader.readFile(filePath));
        } catch(Exception e) {
            if(this.printDebugInfo){
                e.printStackTrace();
            }
        }
    }

    private void convertJSONObjectToCards(@NotNull JSONObject object) throws JSONException {
        JSONArray infectionCards = (JSONArray) object.get("infectionCards");
        for(int i = 0; i < infectionCards.length(); i++){
            this.cards.add(new InfectionCard(infectionCards.get(i).toString()));
        }
    }

    @Override
    public Collection<InfectionCard> deepCopy() {
        ArrayList<InfectionCard> copiedCards = new ArrayList<>();
        for(InfectionCard card : this.cards){
            copiedCards.add(card.deepCopy());
        }
        return copiedCards;
    }
}

