package com.paulnogas.loganalyzer.model

import javafx.beans.property.SimpleStringProperty;

class SearchTextHolder(searchText: String = "") {
    val searchTextProperty = SimpleStringProperty(this, "searchString", searchText)
}
