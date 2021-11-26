package falcon.jacob.pandemichelper.PandemicGame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardColor;
import falcon.jacob.pandemichelper.PandemicGame.Exception.GameException;
import falcon.jacob.pandemichelper.PandemicGame.Interface.IDeck;
import falcon.jacob.pandemichelper.PandemicGame.Util.JsonFileReader;

public class PlayerDeck implements IDeck<PlayerCard> {

    private boolean printDebugInfo;
    protected ArrayList<PlayerCard> cards;
    protected int epidemicCount;

    public void setEpidemicCount(int epidemicCount){
        this.epidemicCount = epidemicCount;
    }

    public PlayerDeck(){
        this.cards = new ArrayList<>();
        this.epidemicCount = 3;
        this.printDebugInfo = false;
        this.load();
    }

    public PlayerDeck(boolean printDebugInfo){
        this.cards = new ArrayList<>();
        this.epidemicCount = 3;
        this.printDebugInfo = printDebugInfo;
        this.load();
    }

    @Override
    public Collection<PlayerCard> getCards() {
        return this.deepCopy();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    @Override
    public PlayerCard draw() throws GameException {
        if(this.checkSize() > 1){
            return this.cards.remove(0);
        } else {
            throw new GameException(GameException.DECK_EMPTY);
        }
    }

    public boolean isEmpty(){
        return this.cards.isEmpty();
    }

    public int checkSize(){
        return this.cards.size();
    }

    public void prepareForGame(){
        int cardsPerGroup = (this.cards.size() / this.epidemicCount);
        int numberOfGroups = (this.cards.size() / cardsPerGroup);
        ArrayList<ArrayList<PlayerCard>> tempList = new ArrayList<>();

        for(int i = 1; i <= numberOfGroups; i++){
            ArrayList<PlayerCard> tempGroupList = new ArrayList<>();
            tempList.add(tempGroupList);
        }

        int counter = 0;
        while(!this.cards.isEmpty()){
            tempList.get(counter).add(this.cards.remove(0));
            if(counter >= tempList.size() - 1){
                counter = 0;
            }else{
                counter++;
            }
        }

        for(ArrayList<PlayerCard> group : tempList){
            group.add(new EpidemicCard());
            Collections.shuffle(group);
            if(this.printDebugInfo){
                System.out.println(group.toString() + " " + group.size());
            }
        }

        Collections.shuffle(tempList);

        for(ArrayList<PlayerCard> group : tempList){
            for(PlayerCard card : group){
                this.cards.add(card);
            }
        }
    }

    @Override
    public void load() {
       this.loadCityCards();
       this.loadEventCards();
       this.shuffle();
    }

    private void loadCityCards(){
        try {
            String filePath = "C:\\Users\\jefal\\AndroidStudioProjects\\PandemicHelper\\app\\src\\main\\java\\falcon\\jacob\\pandemichelper\\PandemicGame\\lib\\cityCards.json";
            JsonFileReader jsonFileReader = new JsonFileReader();
            parseCityObjects(jsonFileReader.readFile(filePath));
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    private void parseCityObjects(JSONObject cities) throws JSONException {
        JSONArray cityCards = (JSONArray) cities.get("cityCards");
        for(int i = 0; i < cityCards.length(); i++){
            this.cards.add(parseCityObject((JSONObject) cityCards.get(i)));
        }
    }

    private CityCard parseCityObject(JSONObject city) throws JSONException {
        String name = (String)city.get("name");
        CardColor color = CardColor.BLUE;
        String cardColor = (String)city.get("color");
        switch(cardColor){
            case "Blue" :
                color = CardColor.BLUE;
                break;
            case "Red" :
                color = CardColor.RED;
                break;
            case "Yellow" :
                color = CardColor.YELLOW;
                break;
            case "Black" :
                color = CardColor.BLACK;
                break;
                default:
        }
        int population = (int)city.get("population");
        return new CityCard(name, color, ((long) population));
    }

    private void loadEventCards(){
        try {
            String filePath = "C:\\Users\\jefal\\AndroidStudioProjects\\PandemicHelper\\app\\src\\main\\java\\falcon\\jacob\\pandemichelper\\PandemicGame\\lib\\eventCards.json";
            JsonFileReader jsonFileReader = new JsonFileReader();
            parseEventObjects(jsonFileReader.readFile(filePath));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void parseEventObjects(JSONObject events) throws JSONException {
        JSONArray eventCards = (JSONArray) events.get("eventCards");
        for(int i = 0; i < eventCards.length(); i++){
            this.cards.add(parseEventObject((JSONObject) eventCards.get(i)));
        }
    }

    private EventCard parseEventObject(JSONObject event) throws JSONException {
        String name = (String) event.get("name");
        return new EventCard(name);
    }

    @Override
    public Collection<PlayerCard> deepCopy() {
        ArrayList<PlayerCard> copiedCards = new ArrayList<>();
        for(PlayerCard card : this.cards){
            copiedCards.add(card.deepCopy());
        }
        return copiedCards;
    }

    @Override
    public String toString() {
        return this.cards.toString();
    }
}