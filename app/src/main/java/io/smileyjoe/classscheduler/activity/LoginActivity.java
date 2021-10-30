package io.smileyjoe.classscheduler.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ActivityLoginBinding;
import io.smileyjoe.classscheduler.utils.Animation;
import io.smileyjoe.classscheduler.utils.Animations;
import io.smileyjoe.classscheduler.utils.Communication;
import io.smileyjoe.classscheduler.utils.Utils;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private Animations mAnimation;

    private final ActivityResultLauncher<Intent> mSignInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            (result) -> onSignInResult(result)
    );

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected ActivityLoginBinding inflate() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = getView().buttonLogin;

        button.setOnClickListener(v -> performLogin());

        mAnimation = Animations.with(this)
                .addAnimation(
                        Animation.on(getView().textLogin)
                                .entry(Animation.Type.TOP))
                .addAnimation(
                        Animation.on(button)
                                .entry(Animation.Type.LEFT))
                .addAnimation(
                        Animation.on(getView().iconMain)
                        .entry(Animation.Type.RIGHT)
                )
                .enter();
    }

    @Override
    public void onBackPressed() {
        mAnimation.exit(this);
    }

    private void performLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = Utils.getAuth().createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build();

        mSignInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else {
            if (response != null) {
                Communication.error(getView().getRoot(), R.string.error_login);
            } else {
                // Do nothing, this means the user cancelled login
            }
        }
    }
}
