package com.example.sakova2

sealed class NavDrawerItem(var route: String, var icon: Int, var title: String) {
    object Home : NavDrawerItem("home", R.drawable.ic_home, "Кафе")
    object About : NavDrawerItem("about", R.drawable.ic_about, "О создателе")
}