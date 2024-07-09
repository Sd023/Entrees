package com.sdapps.entres.main.login.view.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.sdapps.entres.R
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.base.TableActivity
import com.sdapps.entres.main.login.view.compose.theme.EntreésTheme



class LoginNewComposeActivity() : ComponentActivity(), LoginManagerCompose.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val presenter = LoginNewPresenter(this)
        val dbHandler = DBHandler(this)
        dbHandler.createDataBase()

        presenter.attachView(this,applicationContext,dbHandler)
        val firebaseAuth = Firebase.auth


        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            EntreésTheme {

                Surface {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.login_bg_new),
                            contentDescription = "bg",
                        )

                        LoginView(
                            fireabaseAuth = firebaseAuth,
                            presenter = presenter,
                            name = "Login",
                            )

                    }

                }
            }
        }
    }

    override fun showLoading() {
        print("loading")
    }

    override fun hideLoading() {
       print("hideLoading")
    }

    override fun showError(errorMsg: String) {
        print("err")
    }

    override fun navigateToHome() {
        Intent(applicationContext,TableActivity::class.java).also {
            startActivity(it)
        }
    }
}


@Composable
fun LoginView(name: String, fireabaseAuth: FirebaseAuth,presenter: LoginManagerCompose.Presenter) {
    val emailField = remember { mutableStateOf("") }
    val passwordField = remember { mutableStateOf("") }

    val loading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }

    val customStyleFont = FontFamily(
        Font(R.font.righteous_font)
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(10.dp),
            onClick = { /*TODO*/ },
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 40.dp),

            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white),
            ),

            ) {
            Column {
                Row {
                    Text(
                        text = name,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 20.dp)
                            .fillMaxWidth(),
                        fontSize = 30.sp,
                        fontFamily = customStyleFont

                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Column {
                        OutlinedTextField(
                            value = emailField.value,
                            onValueChange = { newText -> emailField.value = newText },
                            label = { Text(text = "Email") },
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .padding(top = 20.dp)
                        )

                        OutlinedTextField(
                            value = passwordField.value,
                            onValueChange = { password -> passwordField.value = password },
                            label = { Text(text = "Password") },
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .padding(top = 20.dp, bottom = 20.dp)

                        )

                        Button(
                            onClick = { proceedLogin(firebaseAuth = fireabaseAuth,presenter,emailField, passwordField) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 30.dp)
                                .size(60.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.bg_color),
                                contentColor = colorResource(id = R.color.white)
                            ))
                        {
                            Text(
                                text = "Sign in",
                                fontSize = 20.sp,
                                fontFamily = customStyleFont,


                                )

                        }

                        if (loading){
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Loading...")
                        }

                        error?.let {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Error: $it", color = androidx.compose.ui.graphics.Color.Red)
                        }
                    }



                }
            }


        }

    }
}

fun proceedLogin(firebaseAuth: FirebaseAuth,presenter: LoginManagerCompose.Presenter,email: MutableState<String>, password: MutableState<String>) {
    if(email.value.isNotEmpty() && password.value.isNotEmpty()){
        presenter.login(firebaseAuth, email.value, password.value)
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EntreésTheme {
        Surface {

            val firebaseAuth = Firebase.auth

            val presenter = LoginNewPresenter(object : LoginManagerCompose.View {
                override fun showLoading() {
                    print("loading")
                }

                override fun hideLoading() {
                    print("hide")
                }

                override fun showError(errorMsg: String) {
                    print("err")
                }

                override fun navigateToHome() {
                    print("home")
                }

            })

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.login_bg_new),
                    contentDescription = "bg",
                )

                LoginView(
                    fireabaseAuth = firebaseAuth,
                    presenter = presenter,
                    name = "Login",
                )

            }
        }

    }
}