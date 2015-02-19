package com.acut.trsplitter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		Log.e("onCreate", "A calcular");
		
		Float floatMessage = Float.parseFloat(message)*100;
		Log.e("onCreate", floatMessage.toString());
		
		String result = makeChangeWrapper(floatMessage.intValue());

		// Create the text view
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(result);

		setContentView(textView);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	private String makeChangeWrapper(Integer amount) {
		int numCoins = 3;
		int[] coins = { 120, 280, 500 };
		int change = 0;

		try {
			change = amount;
		} catch (NumberFormatException e) {
			System.out.println(e);
			System.exit(0);
		}

		int[] used = new int[change + 1];
		int[] last = new int[change + 1];

		makeChange(coins, numCoins, change, used, last);
		
		//StringBuilder sbChange = new StringBuilder();

        int[] ticketsUsed = new int[numCoins];
        for (int i = 0; i < numCoins; i++){
            ticketsUsed[i]=0;
        }
		
		float totalInTickets = 0;
		for( int i = change; i > 0; )
        {
			if (last[i] == 120){
				totalInTickets += 1.2;
                ticketsUsed[0]++; // TODO: Improve the index part
			}else if(last[i] == 280){
				totalInTickets += 2.8;
				ticketsUsed[1]++;
			}else if(last[i] == 500){
				totalInTickets += 5;
				ticketsUsed[2]++;
			}
            i -= last[ i ];
        }
		String resultingChange = "Debes pagar:\n" + ticketsUsed[0] + " de 1.20\n" + ticketsUsed[1] + " de 2.80\n" + ticketsUsed[2] + " de 5\n" + "\nHas pagado " + totalInTickets + " en tickets"; // TODO: Fix this output, it's horrible. 
		if (totalInTickets*100 < amount){
			Float floatAmount = Float.parseFloat(amount.toString())/100;
			Float missingMoney = floatAmount  - totalInTickets;
			resultingChange = resultingChange + "\n\nTe faltan: " + (String.format("%.2f", missingMoney));
		}
		return resultingChange;
	}

	// Dynamic programming algorithm to solve change making problem.
	// As a result, the coinsUsed array is filled with the
	// minimum number of coins needed for change from 0 -> maxChange
	// and lastCoin contains one of the coins needed to make the change.
	public static void makeChange(int[] coins, int differentCoins,
			int maxChange, int[] coinsUsed, int[] lastCoin) {
		coinsUsed[0] = 0;
		lastCoin[0] = 1;

		for (int cents = 1; cents <= maxChange; cents++) {
			int minCoins = cents;
			int newCoin = 1;

			for (int j = 0; j < differentCoins; j++) {
				if (coins[j] > cents) // Cannot use coin j
					continue;
				if (coinsUsed[cents - coins[j]] + 1 < minCoins) {
					minCoins = coinsUsed[cents - coins[j]] + 1;
					newCoin = coins[j];
				}
			}

			coinsUsed[cents] = minCoins;
			lastCoin[cents] = newCoin;
		}
	};

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_result, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
