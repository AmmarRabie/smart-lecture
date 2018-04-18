package cmp.sem.team8.smarlecture.common.auth.firebase;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.auth.AuthenticatedUser;

/**
 * Created by AmmarRabie on 17/04/2018.
 */

/**
 * The implementation of the AuthService using FirebaseAuth.
 * This class is also considered an adapter for the FirebaseAuthentication
 */
public class FirebaseAuthService implements AuthService {


    @Override
    public void signIn(String email, String pass, OnAuthActionComplete callback) {

    }

    @Override
    public void signUp(String name, String email, String pass, OnAuthActionComplete callback) {

    }

    @Override
    public void signOut() {

    }

    @Override
    public void sendPasswordResetEmail(String email, OnAuthActionComplete callback) {

    }

    @Override
    public AuthenticatedUser getCurrentUser() {
        return null;
    }
}
