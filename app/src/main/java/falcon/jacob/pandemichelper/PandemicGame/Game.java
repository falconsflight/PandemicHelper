package falcon.jacob.pandemichelper.PandemicGame;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.*;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;
import falcon.jacob.pandemichelper.PandemicGame.Exception.GameException;

public class Game {
    protected Scanner scanner;
    protected Logger logger;
    protected ArrayList<Player> players;
    protected InfectionDeck infectionDeck;
    protected PlayerDeck playerDeck;
    protected ArrayList<InfectionCard> infectionDiscardPile;
    protected ArrayList<PlayerCard> playerDiscardPile;
    protected int infectionRate;
    protected int epidemicCount; 
    protected boolean oneQuiteNight;
    private boolean printDebugInfo;

    public Game(){
        this.printDebugInfo = false;
        this.infectionDeck = new InfectionDeck();
        this.infectionDiscardPile = new ArrayList<>();
        this.playerDeck = new PlayerDeck();
        this.playerDiscardPile = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<>();
        this.logger = Logger.getLogger(Game.class.getName());
        this.infectionRate = 2;
        this.epidemicCount = 0;
        this.oneQuiteNight = false;
    }

    public Game(boolean printDebugInfo){
        this.printDebugInfo = printDebugInfo;
        this.infectionDeck = new InfectionDeck();
        this.infectionDiscardPile = new ArrayList<>();
        this.playerDeck = new PlayerDeck();
        this.playerDiscardPile = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<>();
        this.logger = Logger.getLogger(Game.class.getName());
        this.infectionRate = 2;
        this.epidemicCount = 0;
        this.oneQuiteNight = false;
    }

    public void setup(){
        this.setupPlayers();
        this.setupPlayerDeck();
        this.intializeInfection();
        this.outputMessage("Game is ready to play!");
    }

    private void setupPlayers(){
        try{
            int playerCount = this.getIntInput("How many players? Minimum - 2 Maximum - 4", 2, 2, 4);
            for(int i = 1; i <= playerCount; i++){
                String name = getInput("Player "+ i +" name?");
                this.players.add(new Player(name));
            }

            //initialize player hands
            for(Player player : this.players){ 
                for(int i = 0; i < (6 - this.players.size()); i++){
                    player.hand.add(this.playerDeck.draw());
                }
                this.outputMessage(player.displayHand());
            }
        }catch(Throwable error){
            this.outputMessage("Unable to setup players! "+ error.getMessage());
        }
    }

    private void setupPlayerDeck(){
        int epidemicAmount = this.getIntInput("How many epidemics? Minimum - 3 Maximum - 6", 3, 3, 6);
        this.playerDeck.setEpidemicCount(epidemicAmount);
        this.playerDeck.prepareForGame();
        
        if(this.printDebugInfo){
            this.outputMessage("Deck after preparing...");
            this.outputMessage(this.playerDeck.toString());
        }
    }

    private void intializeInfection(){
        int diseaseCubes = 1;
        for(int i = 1; i <= 9; i++){
            this.infectCity(diseaseCubes);
            if(i % 3 == 0){
                diseaseCubes++;
            }
        }
    }

    public void play(){
        try{
            int playerCounter = this.findPlayerToStart();
            while(!this.playerDeck.isEmpty()){
                Player currentPlayer = this.players.get(playerCounter);
                int playerChoice = 0;
                do{
                    playerChoice = currentPlayer.takeTurn();
                    switch(playerChoice){
                        case Player.DISPLAY_ACTION : 
                            this.outputMessage(currentPlayer.displayHand());
                            break;
                        case Player.TRADE_ACTION : 
                            this.tradeAction();
                            break;
                        case Player.PLAY_CARD_ACTION : 
                            this.playCardAction(currentPlayer);
                            break;
                        case Player.DISCARD_ACTION : 
                            this.discardCardAction(currentPlayer);
                            break;
                        case Player.DISPLAY_PLAYERS_HANDS_ACTION : 
                            this.displayPlayersHands();
                            break;
                        case Player.DISPLAY_GAME_INFO_ACTION : 
                            this.displayGameInformation();
                            break;
                        case Player.END_TURN_ACTION :
                        default : 
                            playerChoice = Player.END_TURN_ACTION;
                            this.endPlayerTurn(currentPlayer);
                            break;
                    }
                }while(playerChoice != Player.END_TURN_ACTION);
    
                if(playerCounter == this.players.size() - 1){
                    playerCounter = 0;
                } else {
                    playerCounter++;
                }
            }
        }catch(GameException error){
            this.outputMessage("Game over due to... "+error.getMessage());
        }
        this.outputMessage("Game over!");
    }

    public int findPlayerToStart(){
        int playerCounter = 0;
        int playerToStart = 0;
        long highestPopulation = 0;
        for(Player player : this.players){
            long playersHighestPopulation = player.findHighestPopulationCity();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            this.outputMessage(player.getName()+"'s highest population is "+formatter.format(playersHighestPopulation));
            if(playersHighestPopulation > highestPopulation){
                playerToStart = playerCounter;
                highestPopulation = playersHighestPopulation;
            }
            playerCounter++;
        }
        return playerToStart;
    }
    
    public void tradeAction(){
        this.outputMessage("Pick a player to trade to...");
        Player toPlayer = this.choosePlayer();
        this.outputMessage("Pick a player to trade from...");
        Player fromPlayer = this.choosePlayer();
        PlayerCard card = fromPlayer.chooseCard(true);
        if(card != null){
            this.drawCardForPlayer(toPlayer, card);
        }
    }

    public void playCardAction(Player player){
        PlayerCard card = player.chooseCard(true);
        if(card != null && card.getType() == CardType.EVENT){
            this.handleEvent(card.getName(), player);
        }
    }

    public void discardCardAction(Player player){
        PlayerCard card = player.chooseCard(true);
        if(card != null){
            this.playerDiscardPile.add(card);
        }
    }

    public void drawCardForPlayer(Player player, PlayerCard card){
        if(player.hand.size() >= Player.HAND_LIMIT_SIZE){
            this.outputMessage(player.getName()+" must play or discard a card before adding another to their hand.");
            this.playerDiscardPile.add(player.chooseCard(false));
        }
        player.drawCard(card);
    }

    public void endPlayerTurn(Player currentPlayer) throws GameException{
        for(int i = 0; i < 2; i++){
            PlayerCard card = this.playerDeck.draw();
            if(card.getType() == CardType.EPIDEMIC){
                this.handleEpidemic();
            }else{
                this.drawCardForPlayer(currentPlayer, card);
            }
        }
        
        if(!this.oneQuiteNight){
            for(int i = 0; i < this.infectionRate; i++){
                this.infectCity();
            }
        } else {
            this.oneQuiteNight = false;
        }
    }

    public void infectCity(){
        InfectionCard card = this.infectionDeck.draw();
        this.infectCity(card,1);
    }

    public void infectCity(int diseaseCubes){
        InfectionCard card = this.infectionDeck.draw();
        this.infectCity(card,diseaseCubes);
    }

    public void infectCity(InfectionCard card, int diseaseCubes){
        this.outputMessage("Drew Infection card: " + card.toString() + " infected with " + diseaseCubes + " disease cube(s)");
        this.infectionDiscardPile.add(card);
    }

    public void handleEpidemic(){
        this.outputMessage("EPIDEMIC DRAWN");
        this.increase();
        this.checkForPlayerEvent(EventCard.AIRLIFT_EVENT);
        this.checkForPlayerEvent(EventCard.ONE_QUIET_NIGHT_EVENT);
        this.infect();
        this.checkForPlayerEvent(EventCard.RESILIENT_POPULATION_EVENT);
        this.checkForPlayerEvent(EventCard.FORECAST_EVENT);
        this.intensify();
    }

    public void increase(){
        this.epidemicCount++;
        //increase infection rate
        if(this.epidemicCount == 3 || this.epidemicCount == 5){
            this.infectionRate++;
            this.outputMessage("Infection Rate increased to "+this.infectionRate);
        }
    }

    public void infect(){
        InfectionCard card = this.infectionDeck.drawBottomCard();
        this.outputMessage(card.toString()+" drawn from bottom of the infection deck");
        if(!this.oneQuiteNight){
            this.infectCity(card, 3);
        }
    }

    public void intensify(){
        Collections.shuffle(this.infectionDiscardPile);
        int discardPileSize = this.infectionDiscardPile.size();
        for(int index = discardPileSize-1; index >= 0; index--){
            this.infectionDeck.addCardToDeck(this.infectionDiscardPile.remove(index));
        }
    }

    public void checkForPlayerEvent(String event){
        Player player = this.findPlayerWithCard(event);
        if(player != null){
            if(this.getIntInput("Would "+player.getName()+" like to use their "+event+"?\n1.Yes\n2.No", 0, 1, 2) == 1){
                this.handleEvent(event, player);
            }
        }
    }

    public void handleEvent(String event, Player player){
        switch(event){
            case EventCard.ONE_QUIET_NIGHT_EVENT:
                this.handleOneQuietNight(player);
                break;
            case EventCard.RESILIENT_POPULATION_EVENT:
                this.handleResilientPopulation(player);
                break;
            case EventCard.FORECAST_EVENT:
                this.handleForecast(player);
                break;
            case EventCard.AIRLIFT_EVENT:
                this.handleAirlift(player);
                break;
            default:
                break;
        }
    }

    public Player choosePlayer(){
        int choice = this.chooseFromList(this.players);
        return this.players.get(choice);
    }

    private <T> int chooseFromList(ArrayList<T> list){
        int counter = 1;
        this.outputMessage("Which would you like to choose?");
        for(T item : list){
            this.outputMessage(counter+". "+item.toString()+"\n");
            counter++;
        }
        return this.getIntInput("",0,1,counter) - 1;
    }

    public void displayPlayersHands(){
        for(Player player : this.players){
            this.outputMessage(player.displayHand());
        }
    }

    public void displayGameInformation(){
        this.outputMessage("Infection Rate: " + this.infectionRate);
        this.outputMessage("Epidemic Count: " + this.epidemicCount);
        this.outputMessage("Infection Discard Pile: " + this.infectionDiscardPile.toString() + " \n\tCard count: " + this.infectionDiscardPile.size());
        this.outputMessage("Player Discard Pile: " + this.playerDiscardPile.toString() + " \n\tCard count: " + this.playerDiscardPile.size() + this.getColorCounts());
    }

    public String getColorCounts(){
        int blue = 0;
        int red = 0;
        int yellow = 0;
        int black = 0;
        for(PlayerCard card : this.playerDiscardPile){
            if(card.getType() == CardType.CITY){
                CityCard city = (CityCard)card;
                switch(city.getColor()){
                    case BLUE :
                        blue++;
                        break;
                    case RED :
                        red++;
                        break;
                    case YELLOW :
                        yellow++;
                        break;
                    case BLACK :
                        black++;
                        break;
                    default:
                }
            }
        }
        return "\n\tBlue: "+blue+"\n\tRed: "+red+"\n\tYellow: "+yellow+"\n\tBlack: "+black;
    }

    public Player findPlayerWithCard(String name){
        for(Player player : this.players){
            for(PlayerCard card : player.hand){
                if(card.getName().equals(name)){
                    return player;
                }
            }
        }
        return null;
    }

    public void handleOneQuietNight(Player player){
        this.playerDiscardPile.add(player.playCard(EventCard.ONE_QUIET_NIGHT_EVENT));
        this.oneQuiteNight = true;
    }

    public void handleForecast(Player player){
        this.playerDiscardPile.add(player.playCard(EventCard.FORECAST_EVENT));
        ArrayList<InfectionCard> cards = new ArrayList<>();
        //draw the top six
        for(int i = 0; i < 6; i++){
            cards.add(this.infectionDeck.draw());
        }
        while(!cards.isEmpty()){
            this.outputMessage("Choose card to place on top of the infection deck");
            int choice = this.chooseFromList(cards);
            this.infectionDeck.addCardToDeck(cards.remove(choice));
            if(cards.size() == 1){
                this.infectionDeck.addCardToDeck(cards.remove(0));
                this.infectionDiscardPile = new ArrayList<>();
            }
        }
    }

    public void handleResilientPopulation(Player player){
        this.playerDiscardPile.add(player.playCard(EventCard.RESILIENT_POPULATION_EVENT));
        int choice = this.chooseFromList(this.infectionDiscardPile);
        this.infectionDiscardPile.remove(choice);
    }

    public void handleAirlift(Player player){
        this.playerDiscardPile.add(player.playCard(EventCard.AIRLIFT_EVENT));
    }

    public String getInput(String message){
        outputMessage(message);
        return this.scanner.nextLine();
    }

    public int getIntInput(String message, int defaultValue, int minimum, int maximum){
        boolean valid = false;
        int returnValue = defaultValue;
        while(!valid){
            try{
                this.outputMessage(message);
                returnValue = Integer.parseInt(this.scanner.nextLine());
                if(returnValue >= minimum && returnValue <= maximum){
                    valid = true;
                }else{
                    this.outputMessage("Value must be between "+minimum+" and "+maximum);
                }
            }catch(Throwable error){
                this.outputMessage("Invalid value...");
                valid = false;
            }
        }
        return returnValue;
    }

    public void outputMessage(String message){
        System.out.println(message);
    }
}