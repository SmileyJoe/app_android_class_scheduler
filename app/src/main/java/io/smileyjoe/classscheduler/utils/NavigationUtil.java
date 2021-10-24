package io.smileyjoe.classscheduler.utils;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationUtil {

    /**
     * This is a copy of {@link NavigationUI#setupWithNavController(BottomNavigationView, NavController)}
     * <br/>
     * This allows a custom {@link NavigationBarView.OnItemSelectedListener} where processing the click
     * can be ignored. Using the {@link NavigationUI} way meant that the fragment would
     * load, then the redirecting action would go off, either from the fragment, or from the
     * {@link NavController.OnDestinationChangedListener}. This caused a flash of the view to change,
     * then the redirecting activity to show, which I didn't like.
     * <br/>
     * This lets you specify a {@link NavigationBarView.OnItemSelectedListener}, process the click
     * and then return false to ignore it, or true to continue as the {@link NavigationUI} would.
     * <b/>
     * An example is clicking on the account option in the {@link BottomNavigationView}, checking
     * if the user is logged in, and then either showing a login activity, or showing the account
     * fragment.
     *
     * @param bottomNavigationView The BottomNavigationView that should be kept in sync with
     *                             changes to the NavController.
     * @param navController The NavController that supplies the primary menu.
     *                      Navigation actions on this NavController will be reflected in the
     *                      selected item in the BottomNavigationView.
     * @param listener
     */
    public static void setupWithNavController(
            @NonNull final BottomNavigationView bottomNavigationView,
            @NonNull final NavController navController,
            NavigationBarView.OnItemSelectedListener listener) {

        // Copied from {@link NavigationUI#setupWithNavController(BottomNavigationView, NavController)}
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Menu menu = bottomNavigationView.getMenu();
            for (int h = 0, size = menu.size(); h < size; h++) {
                MenuItem item = menu.getItem(h);
                if (matchDestination(destination, item.getItemId())) {
                    item.setChecked(true);
                }
            }
        });

        // Check the return from the passed in listener and continue if need be //
        bottomNavigationView.setOnItemSelectedListener(item -> {
            boolean proceed = true;

            if(listener != null){
                proceed = listener.onNavigationItemSelected(item);
            }

            if(proceed){
                NavigationUI.onNavDestinationSelected(item, navController);
            }

            return proceed;
        });
    }

    /**
     * Taken from {@see NavigationUI#matchDestination(NavDestination, int)} because it
     * has package level access for some reason.
     *
     */
    private static boolean matchDestination(@NonNull NavDestination destination,
                                     @IdRes int destId){
        NavDestination currentDestination = destination;
        while (currentDestination.getId() != destId && currentDestination.getParent() != null) {
            currentDestination = currentDestination.getParent();
        }
        return currentDestination.getId() == destId;
    }

}
