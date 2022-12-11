package com.example.bettingpredictions.presentation.home.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bettingpredictions.R
import com.example.bettingpredictions.presentation.home.view.dialog.CustomDialog
import com.example.bettingpredictions.presentation.home.viewmodel.HomeViewModel
import com.example.bettingpredictions.presentation.ui.theme.ColorPrimary
import com.example.bettingpredictions.util.Screen


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    navController: NavHostController?,
    homeViewModel: HomeViewModel?,
) {
    val matchDataList = homeViewModel?.matchDataList?.collectAsState(initial = emptyList())

    val showDialog = remember { mutableStateOf(false) }
    val showSelectPos = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        homeViewModel?.getMatchData()
    }

    if (showDialog.value)
        CustomDialog(match = matchDataList?.value!![showSelectPos.value], setShowDialog = {
            showDialog.value = it
        }) {
            homeViewModel.updateMatch(it)
        }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 11.dp)
                .padding(horizontal = 11.dp)
                .align(Alignment.End)
                .clickable {
                    navController?.navigate(Screen.ResultScreenRoute.route) {
                        navController.popBackStack()
                    }
                },
            text = stringResource(id = R.string.Get_results),
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,

            )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.7.dp),
            color = Color.White,
            thickness = 1.dp
        )

        if (matchDataList?.value!!.isNotEmpty()) {
            Column(
                modifier = Modifier.verticalScroll(
                    state = rememberScrollState(),
                    enabled = true
                )
            ) {
                for (match in matchDataList.value) {
                    Column(modifier = Modifier.clickable {
                        showSelectPos.value = matchDataList.value.indexOf(match)
                        showDialog.value = true
                    }
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Row {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 5.dp),
                                text = match.team1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                                textAlign = TextAlign.End
                            )

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp),
                                text = match.team1_bet_points.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp),
                                text = stringResource(id = R.string.minus),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp),
                                text = match.team2_bet_points.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )

                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 5.dp),
                                text = match.team2,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                                textAlign = TextAlign.Start
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.7.dp),
                        color = Color.White,
                        thickness = 1.dp
                    )

                }
            }
        }
    }

    if (homeViewModel?.loadApi?.value == true) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            ) {
                CircularProgressIndicator(color = ColorPrimary)
            }
        }
    }
}

