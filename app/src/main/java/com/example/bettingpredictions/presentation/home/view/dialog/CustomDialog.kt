package com.example.bettingpredictions.presentation.home.view.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bettingpredictions.R
import com.example.bettingpredictions.domain.model.MatchesData
import com.example.bettingpredictions.presentation.ui.theme.DialogGray

@Composable
fun CustomDialog(
    match: MatchesData,
    setShowDialog: (Boolean) -> Unit,
    setValue: (MatchesData) -> Unit
) {
    val team1BetPoints = remember { mutableStateOf(match.team1_bet_points) }
    val team2BetPoints = remember { mutableStateOf(match.team2_bet_points) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DialogGray
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 3.dp),
                        text = match.team1,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .clickable {
                                    var point = team1BetPoints.value
                                    if (point > 0) {
                                        point--
                                        team1BetPoints.value = point
                                    }
                                },
                            text = stringResource(id = R.string.minus),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp),
                            text = team1BetPoints.value.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .clickable {
                                    var point = team1BetPoints.value
                                    point++
                                    team1BetPoints.value = point
                                },
                            text = stringResource(id = R.string.plus),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .clickable {
                                    var point = team2BetPoints.value
                                    if (point > 0) {
                                        point--
                                        team2BetPoints.value = point
                                    }
                                },
                            text = stringResource(id = R.string.minus),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp),
                            text = team2BetPoints.value.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .clickable {
                                    var point = team2BetPoints.value
                                    point++
                                    team2BetPoints.value = point
                                },
                            text = stringResource(id = R.string.plus),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 3.dp),
                        text = match.team2,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            match.team1_bet_points = team1BetPoints.value
                            match.team2_bet_points = team2BetPoints.value
                            setValue(match)
                            setShowDialog(false)
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .padding(horizontal = 13.dp)
                            .height(33.dp)
                    ) {
                        Text(text = stringResource(R.string.dialog_button_text_enter))
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

        }
    }
}