package com.example.sakova2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// главное активити
class MainActivity : ComponentActivity() {

    // все кафе в виде json
    val jsonFileString = """
        [{"id":0,"name":"Дом восемь","menu":["Гренки с чесноком и сыром","Кольца кальмара","Куриные крылышки в медово-горчичном соусе","Сырные палочки"],"address":"просп.Маркса, 8"},{"id":1,"name":"Неони","menu":["Горячий ролл с тунцом","Ролл с тунцом","Обеденное предложение","Роза-личи"],"address":"просп. Ленина, 137, корп. 4"},{"id":2,"name":"Атмосфера Рестобар","menu":["Цыпленок, запеченный в пряной глазури","Бефстроганов","Говядина по-бургундски с картофельным гратеном"],"address":"ул.Курчатова, 31А"},{"id":3,"name":"Веранда","menu":["Салат с ростбифом","Мясное ассорти"],"address":"просп.Маркса, 45"},{"id":4,"name":"Клён","menu":["Цезарь"],"address":"просп.Маркса, 46А"},{"id":5,"name":"Диканька","menu":["Борщ украинский","Куриная лапша","Говяжий язык со шпинатом в сливочном соусе","Гуляш из говядины","Котлета по-киевски","Жаркое из кролика в сметане"],"address":"просп.Ленина, 128А"},{"id":6,"name":"География","menu":["Пицца Александро","Пицца Баварезе","Пицца Поло"],"address":"просп.Ленина, 57"},{"id":7,"name":"Тифлисъ","menu":["Ассорти шашлыков","Дорадо","Картофель на мангале","Люля кебаб из баранины"],"address":"просп.Маркса, 130"},{"id":8,"name":"Джонга","menu":["Кимчи Тиге со свининой","Суп Кукси","Суши креветка","Горячий сет","Тубу Кимчи","Токпокки"],"address":"просп.Маркса, 20"},{"id":9,"name":"Бакинский дворик","menu":["Шашлык из свиной корейки","Люля-кебаб из картофеля","Шашлык Короглу","Долма с телятиной","Джиз быз","Плов Сабза"],"address":"ул.Борисоглебская, 58А"},{"id":10,"name":"Радар","menu":["Колбаски фри","Луковые кольца","Бургер Классический"],"address":"просп.Маркса, 79"},{"id":11,"name":"Мельница","menu":["Аджапсандали","Ассорти из солений","Говяжий язык","Кучмачи","Гоми","Копченый сулугуни с помидорами","Сациви из курицы"],"address":"ул.Курчатова, 41"},{"id":12,"name":"Бургер Кинг","menu":["Гранд Чиз Пара","Воппер Кинг Комбо","Комбо на двоих с Ангус 4 сыра","Наггетсы"],"address":"ул.Курчатова, 55"},{"id":13,"name":"Чайхона","menu":["Лагман уйгурский","Казан-кебаб с бараниной","Мастава","Манты","Куриные крылышки","Каре ягненка"],"address":"ул.Энгельса, 9Б"},{"id":14,"name":"Томато","menu":["Пицца Деревенская","Пицца Терияки с беконом","Пицца Миланская","Пицца Баварская"],"address":"ул.Энгельса, 9Б"}]
        """.trimIndent()

    // вызывается при создании главного экрана (запуске приложения)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // отображаем содержимое главного экрана
            MainScreen()
        }
    }

    // содержимое главного экрана
    @Composable
    fun MainScreen() {
        // вспомогательные элементы для бокового меню
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        // контроллер для перехода между экранами
        val navController = rememberNavController()
        Scaffold(
            scaffoldState = scaffoldState,
            // устанавливаем верхний бар приложения (содержимое описано ниже)
            topBar = {
                myTopBar(scope = scope, scaffoldState = scaffoldState)
            },
            drawerContent = {
                // содержимое основного экрана
                Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
            },
            backgroundColor = colorResource(id = android.R.color.system_accent1_100)
        )
        { padding ->  // We need to pass scaffold's inner padding to content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                Navigation(navController = navController)
            }
        }
    }

    @Composable
    fun myTopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = "ObninsKafe by Анастасия",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Поиск",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            },
            backgroundColor = Color.Gray,
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                ) {
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        )
    }

    @Composable
    fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
        val screens = listOf(
            NavDrawerItem.Home,
            NavDrawerItem.About,
        )
        Column(
            modifier = Modifier
                .background(colorResource(id = android.R.color.system_accent1_100))
        ) {
            // Header
            Image(
                painter = painterResource(id = android.R.drawable.ic_dialog_map),
                contentDescription = android.R.drawable.ic_dialog_map.toString(),
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
            )
//             Space between
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
            // List of navigation items
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            screens.forEach { item ->
                DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                    // Close drawer
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Developed by Миронова Анастасия",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }

    @Composable
    fun DrawerItem(item: NavDrawerItem, selected: Boolean, onItemClick: (NavDrawerItem) -> Unit) {
        val background = if (selected) R.color.gray else android.R.color.transparent
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onItemClick(item) })
                .height(45.dp)
                .background(colorResource(id = background))
                .padding(start = 10.dp)
        ) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = item.title,
                colorFilter = ColorFilter.tint(Color.Black),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(35.dp)
                    .width(35.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = item.title,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController, startDestination = NavDrawerItem.Home.route) {
            composable(NavDrawerItem.Home.route) {
                HomeScreen()
            }
            composable(NavDrawerItem.About.route) {
                AboutScreen()
            }
        }
    }

    val gson = Gson()
    val listPersonType = object : TypeToken<List<GridItem>>() {}.type
    var kafes: List<GridItem> = gson.fromJson(jsonFileString, listPersonType)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun HomeScreen() {

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(5.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                kafes.forEach { receipt ->
                    item {
                        Item(item = receipt)
                    }
                }
            }
    }

    data class GridItem(
        val id: Int,
        val name: String,
        val menu: List<String>,
        val address: String
    ){}

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Item(
        modifier: Modifier = Modifier,
        item: GridItem
    ) {
        val mContext = LocalContext.current
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp,
            onClick = {
                val intent = Intent(mContext, CookActivity::class.java)
                intent.putExtra("gridItem", item.address)
                mContext.startActivity(intent)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.cardColor))
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Cursive
                )
                item.menu.forEach{ ingredient ->
                    Text(
                        text = "* $ingredient",
                        textAlign = TextAlign.Left,
                        fontSize = 14.sp
                    )
                }

            }
        }
    }

    @Composable
    fun AboutScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {
            Text(
                text = "О создателе",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            )
            Text(
                text = "Приложение создано в качестве Третьей лабораторной работы, Мироновой Анастасией",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
            )
        }
    }
}


