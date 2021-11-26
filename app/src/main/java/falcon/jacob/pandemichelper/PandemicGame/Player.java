package falcon.jacob.pandemichelper.PandemicGame;

import java.util.ArrayList;
import java.util.Scanner;

import falcon.jacob.pandemichelper.PandemicGame.Enum.CardType;

public class Player {
    public static final int HAND_LIMIT_SIZE = 7;
    
    public static final int DISPLAY_ACTION = 1;
    public static final int TRADE_ACTION = 2;
    public static final int PLAY_CARD_ACTION = 3;
    public static final int DISCARD_ACTION = 4;
    public static final int DISPLAY_PLAYERS_HANDS_ACTION = 5;
    public static final int DISPLAY_GAME_INFO_ACTION = 6;
    public static final int END_TURN_ACTION = 7;
    
    protected ArrayList<PlayerCard> hand;
    protected String name;
    protected Scanner scanner;

    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public String getName(){
        return this.name;
    }

    public String displayHand(){
        return this.name + "'s cards: " + this.hand.toString() + " \n\tCard count: " + this.hand.size() + this.getColorCounts();
    }

    public String getColorCounts(){
        int blue = 0;
        int red = 0;
        int yellow = 0;
        int black = 0;
        for(PlayerCard card : this.hand){
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

    public int takeTurn(){
        this.displayPlayerMenu();
        return this.getIntInput();
    }

    public void displayPlayerMenu(){
        this.outputMessage(this.name+"'s turn. Please choose an option from the menu:");
        this.outputMessage("1. Show Cards\n2. Trade Card\n3. Play Card\n4. Discard Card\n5. Show other player's cards\n6. Display game information\n7. End turn");
    }

    public long findHighestPopulationCity(){
        long population = 0;
        for(PlayerCard card : this.hand){
            if(card.getType() == CardType.CITY){
                CityCard city = (CityCard)card;
                if(population < city.getPopulation()){
                    population = city.getPopulation();
                }
            }
        }
        return population;
    }

    public PlayerCard discardCard(int index){
        return this.hand.remove(index);
    }

    public PlayerCard playCard(int index){
        return this.hand.remove(index);
    }

    public PlayerCard playCard(String name){
        for(int index = 0; index < this.hand.size(); index++){
            if(this.hand.get(index).getName().equals(name)){
                return this.hand.remove(index);
            }
        }
        return null;
    }

    public void drawCard(PlayerCard card){
        this.outputMessage(this.name+" picked up "+card.toString());
        this.hand.add(card);
    }

    public int getIntInput(){
        return Integer.parseInt(this.scanner.nextLine());
    }

    public PlayerCard chooseCard(boolean exitAllowed){
        int counter = 1;
        this.outputMessage("Which card would you like to chooose?");
        for(PlayerCard card : this.hand){
            this.outputMessage(counter+". "+card.toString()+"\n");
            counter++;
        }
        if(exitAllowed){
            this.outputMessage(counter+". Exit");
        }
        int choice = this.getIntInput();

        if(choice == counter){
            return null;
        }else{
            return this.hand.remove(choice - 1);
        }
    }

    @Override
    public String toString(){
        return this.name;
    }

    public void outputMessage(String message){
        System.out.println(message);
    }
}