package com.example.bettingpredictions.presentation.result.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.bettingpredictions.presentation.result.viewmodel.ResultViewModel
import com.example.bettingpredictions.presentation.ui.theme.ColorPrimary
import com.example.bettingpredictions.util.Screen

@Composable
fun ResultScreen(
    navController: NavHostController?,
    resultViewModel: ResultViewModel?,
) {
    val matchList = resultViewModel?.matchDataList?.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 11.dp)
                .padding(horizontal = 11.dp)
                .align(Alignment.End)
                .clickable {
                    resultViewModel?.restoreData()
                    navController?.navigate(Screen.HomeScreenRoute.route)
                },
            text = stringResource(id = R.string.Restore),
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
        )

        LaunchedEffect(Unit) {
            resultViewModel?.getResulData()
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.6.dp),
            color = Color.White,
            thickness = 1.dp
        )

        if (matchList?.value!!.size > 0) {
            Column(
                modifier = Modifier.verticalScroll(
                    state = rememberScrollState(),
                    enabled = true
                )
            ) {
                for (match in matchList.value) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row() {
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
                            text = match.team1_points.toString(),
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
                            text = match.team2_points.toString(),
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 3.dp),
                            text = match.team1_bet_points.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 2.dp),
                            text = stringResource(id = R.string.minus),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 3.dp),
                            text = match.team2_bet_points.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.6.dp),
                        color = Color.White,
                        thickness = 1.dp
                    )

                }
            }
        }
    }

    if (resultViewModel?.loadApi?.value == true) {
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