import mode.*;

import java.util.Scanner;

public class LegendsGame {
    public static void main(String[] args){
        GameMode mode = chooseMode();
        if (mode == null){
            return;
        }
        mode.start();
    }

    private static GameMode chooseMode(){
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.println("Select a game mode:");
            System.out.println("1. Monsters vs Heroes");
            System.out.println("2. Legends of Valor");

            String choice = input.nextLine().trim().toLowerCase();
            if (choice.equals("q")){
                return null;
            }
            int mode;
            try{
                mode = Integer.parseInt(choice);
            } catch(NumberFormatException e){
                System.out.println("Please enter a valid choice");
                continue;
            }

            switch (mode){
                case 1:
                    return new MonstersAndHeroesMode();
                case 2:
                    return new LegendsOfValorMode();
                default:
                    System.out.println("Invalid input! Please try again");
            }
        }
    }
}