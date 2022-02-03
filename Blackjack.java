public class Blackjack {

	private static BlackjackCards shoe;
	private static BlackjackCards dealer;
	private static BlackjackCards player;
	private static BlackjackCards discard;
	private int playerWins;
	private int push;
	private int dealerWins;
	private int roundsPlayed;
	private int roundsTraced;
	private int roundsDeckNum;

	public static void main(String[] args) {
		final int roundsPlayed = Integer.parseInt(args[0]);
		final int numDecks = Integer.parseInt(args[1]);
		final int roundsTraced = Integer.parseInt(args[2]);

		Blackjack start = new Blackjack(roundsPlayed, numDecks, roundsTraced);
		start.play();

	}


	public Blackjack (int rounds, int decks, int trace) {
		//Construct the game
		int shoeSize = decks * 52;

		roundsPlayed = rounds;
		roundsDeckNum = decks;
		roundsTraced = trace;


		discard = new BlackjackCards(shoeSize);
		shoe = new BlackjackCards(shoeSize);
		player = new BlackjackCards(25);
		dealer = new BlackjackCards(25);

		for(Card.Suits s:Card.Suits.values()) {
			for(Card.Ranks r:Card.Ranks.values()) {
				for(int i=0;i<decks;i++) {
					shoe.enqueue(new Card(s,r)); // Enqueue all 52 cards into each shoe 
				}
			}
		}
		shoe.shuffle();
	}


	private void play() {
		System.out.println(" Starting Blackjack with " + roundsPlayed + " and " + roundsDeckNum + " decks in the shoe" + "\n");
		for(int i=1;i<=roundsPlayed;i++) {
			boolean trace;
			if(i<=roundsTraced) {
				trace = true;
				System.out.println("\n");
			}
			else trace = false;

			this.round(trace);


			//Replenish the shoe after the round
			if(shoe.size() <= (roundsDeckNum * 52) / 4) {
				for(int b=0;b<discard.size();b++) {
					shoe.enqueue(discard.get(b)); //return cards from deck to shoe
				}
				discard.clear();
				shoe.shuffle();
				System.out.println("Reshuffling the shoe in round " + i + "\n");
			}

		}
		System.out.println("After " + roundsPlayed + " Rounds, here are the results:");
		System.out.println("\t Dealer wins: " + dealerWins);
		System.out.println("\t Player wins: " + playerWins);
		System.out.println("\t Pushes: " + push);
	}

	private void returnCards() {
		while(player.size() > 0) {
			discard.enqueue(player.dequeue());
		}
		while(dealer.size() > 0) {
			discard.enqueue(dealer.dequeue());
		}
	}

	private void round(boolean trace) {

		
		//deal two cards alternating
		player.enqueue(shoe.dequeue());
		dealer.enqueue(shoe.dequeue());
		player.enqueue(shoe.dequeue());
		dealer.enqueue(shoe.dequeue());


		if(trace) {
			System.out.println("Player: " + player.toString() + ": " + player.getValue());
			System.out.println("Dealer: " + dealer.toString() + ": " + dealer.getValue());
		}


		//Initial blackjack case
		if(player.getValue() == 21 && dealer.getValue() == 21) {
			if(trace) {
				System.out.println("Result: Push!");
			}
			push++;
			this.returnCards();
			return;
		}
		if(player.getValue() == 21 && dealer.getValue() != 21) {
			if(trace) {
				System.out.println("Result: Player Blackjack Wins!");
			}
			playerWins++;
			this.returnCards();
			return;
		}
		if(player.getValue() != 21 && dealer.getValue() == 21) {
			if(trace) {
				System.out.println("Result: Dealer Blackjack Wins!");
			}
			dealerWins++;
			this.returnCards();
			return;
		}

		//Regular play case
		while(player.getValue() < 17) {
			player.enqueue(shoe.getFront());
			if(trace) {
				System.out.println("Player hits: " + shoe.getFront().toString());
			}
			shoe.dequeue();
		}


		//Busted after drawing 
		if(player.getValue() > 21) {
			if(trace) {
				System.out.println("Player BUSTS: " + player.toString() + ": " + player.getValue());
				System.out.println("Result: Dealer wins!");
			}
			dealerWins++;
			this.returnCards();
			return;
		}

		if(player.getValue() >= 17 && player.getValue() <= 21) {
			if(trace) {
				System.out.println("Player STANDS: " + player.toString() + ": " + player.getValue());
			}
		}

		while(dealer.getValue() < 17) {
			if(trace) {
				System.out.println("Dealer Hits: " + shoe.getFront().toString());
			}
			dealer.enqueue(shoe.getFront());
			shoe.dequeue();
		}

		if(dealer.getValue() > 21) {
			this.returnCards();
			playerWins++;
			return;
		}

		
		//dealer.getValue() >= 17 && originally, but the while loop will ensure this condition is always met. 
		if(dealer.getValue() <= 21) {
			if(trace) System.out.println("Dealer STANDS: " + dealer.toString() + ": " + dealer.getValue());
		}



		//Final case
		if(player.getValue() > dealer.getValue()) {
			if(trace) System.out.println("Result: Player wins!");
			this.returnCards();
			playerWins++;
			return;
		}
		if(player.getValue() < dealer.getValue()) {
			if(trace) System.out.println("Result: Dealer wins!");
			this.returnCards();
			dealerWins++;
			return;
		}
		else {
			if(trace) System.out.println("Result: Push!");
			this.returnCards();
			push++;
			return;
		}
	}
}
