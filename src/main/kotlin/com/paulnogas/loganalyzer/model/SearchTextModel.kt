package com.paulnogas.loganalyzer.model

import javafx.beans.property.ReadOnlyStringProperty
import tornadofx.ItemViewModel

class SearchTextModel(searchHolder: SearchTextHolder) : ItemViewModel<SearchTextHolder>(searchHolder) {
    val modelSearchText = bind(SearchTextHolder::searchTextProperty) as ReadOnlyStringProperty
}
