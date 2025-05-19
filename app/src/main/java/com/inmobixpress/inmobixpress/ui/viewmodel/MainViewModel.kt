package com.inmobixpress.inmobixpress.ui.viewmodel

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Session
import com.google.maps.android.SphericalUtil
import com.inmobixpress.inmobixpress.data.network.model.Device
import com.inmobixpress.inmobixpress.data.network.model.Image
import com.inmobixpress.inmobixpress.data.network.model.Property
import com.inmobixpress.inmobixpress.data.network.model.PropertyHasOfferType
import com.inmobixpress.inmobixpress.data.network.model.Publishing
import com.inmobixpress.inmobixpress.data.network.model.Request
import com.inmobixpress.inmobixpress.data.network.model.RequestHasPublishing
import com.inmobixpress.inmobixpress.data.network.model.RequestState
import com.inmobixpress.inmobixpress.data.network.model.RequestType
import com.inmobixpress.inmobixpress.data.network.model.User
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.model.AnchorItem
import com.inmobixpress.inmobixpress.ui.model.District
import com.inmobixpress.inmobixpress.ui.model.FilterType
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.model.ServiceMarker
import com.inmobixpress.inmobixpress.ui.model.UIState
import com.inmobixpress.inmobixpress.ui.utils.hourToMillis
import com.inmobixpress.inmobixpress.ui.utils.millisToLocalDateTime
import com.inmobixpress.inmobixpress.ui.utils.today
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PropertyRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _loadingVisible = MutableLiveData<Boolean>()
    val loadingVisible: LiveData<Boolean> = _loadingVisible

    private val _bottomBarVisible = MutableLiveData<Boolean>()
    val bottomBarVisible: LiveData<Boolean> = _bottomBarVisible

    private val _contactBottomBarVisible = MutableLiveData<Boolean>()
    val contactBottomBarVisible: LiveData<Boolean> = _contactBottomBarVisible

    private val _trackerMapVisible = MutableLiveData<Boolean>()
    val trackerMapVisible: LiveData<Boolean> = _trackerMapVisible

    private val _whatsappBottomSheetVisible = MutableLiveData<Boolean>()
    val whatsappBottomSheetVisible: LiveData<Boolean> = _whatsappBottomSheetVisible

    private val _contactBottomSheetVisible = MutableLiveData<Boolean>()
    val contactBottomSheetVisible: LiveData<Boolean> = _contactBottomSheetVisible

    private val _trackerBottomSheetVisible = MutableLiveData<Boolean>()
    val trackerBottomSheetVisible: LiveData<Boolean> = _trackerBottomSheetVisible

    private val _tabIndex = MutableLiveData<Int>()
    val tabIndex: LiveData<Int> = _tabIndex

    private val _visitDayDialogVisible = MutableLiveData<Boolean>()
    val visitDayDialogVisible: LiveData<Boolean> = _visitDayDialogVisible

    private val _confirmDialogVisible = MutableLiveData<Boolean>()
    val confirmDialogVisible: LiveData<Boolean> = _confirmDialogVisible

    private val _requestDialogVisible = MutableLiveData<Boolean>()
    val requestDialogVisible: LiveData<Boolean> = _requestDialogVisible

    private val _errorDialogVisible = MutableLiveData<Boolean>()
    val errorDialogVisible: LiveData<Boolean> = _errorDialogVisible

    private val _completeDialogVisible = MutableLiveData<Boolean>()
    val completeDialogVisible: LiveData<Boolean> = _completeDialogVisible

    private val _visitDialogVisible = MutableLiveData<Boolean>()
    val visitDialogVisible: LiveData<Boolean> = _visitDialogVisible

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _nameError = MutableLiveData<Boolean>()
    val nameError: LiveData<Boolean> = _nameError

    private val _nameMessageError = MutableLiveData<String>()
    val nameMessageError: LiveData<String> = _nameMessageError

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _emailError = MutableLiveData<Boolean>()
    val emailError: LiveData<Boolean> = _emailError

    private val _emailMessageError = MutableLiveData<String>()
    val emailMessageError: LiveData<String> = _emailMessageError

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _phoneError = MutableLiveData<Boolean>()
    val phoneError: LiveData<Boolean> = _phoneError

    private val _phoneMessageError = MutableLiveData<String>()
    val phoneMessageError: LiveData<String> = _phoneMessageError

    private val _dni = MutableLiveData<String>()
    val dni: LiveData<String> = _dni

    private val _dniError = MutableLiveData<Boolean>()
    val dniError: LiveData<Boolean> = _dniError

    private val _dniMessageError = MutableLiveData<String>()
    val dniMessageError: LiveData<String> = _dniMessageError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _messageError = MutableLiveData<Boolean>()
    val messageError: LiveData<Boolean> = _messageError

    private val _messageTextError = MutableLiveData<String>()
    val messageTextError: LiveData<String> = _messageTextError

    private val _visitDay = MutableLiveData<String>(today())
    val visitDay: LiveData<String> = _visitDay

    private val _visitLocal = MutableLiveData<LocalDateTime>()
    val visitLocal: LiveData<LocalDateTime> = _visitLocal

    private val _visitMillis = MutableLiveData<Long>()
    val visitMillis: LiveData<Long> = _visitMillis

    private val _timeTable = MutableLiveData<String>()
    val timeTable: LiveData<String> = _timeTable

    private val _priorityType = MutableLiveData<String>()
    val priorityType: LiveData<String> = _priorityType

    val serviceMarkers = mutableStateMapOf<String, ServiceMarker>()

    val foundProperties = mutableStateMapOf<String, PropertyItem>()

    val nearbyProperties = mutableStateMapOf<String, AnchorItem>()

    private val _districts = MutableLiveData<List<District>>(emptyList())
    val districts: LiveData<List<District>> = _districts

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val dist = flow {
        while (true) emit(districts.value!!)
    }

    var searchQuery by mutableStateOf("")
        private set

    val searchResults: StateFlow<List<District>> =
        snapshotFlow { searchQuery }
            .combine(dist) { searchQuery, districts ->
                when {
                    searchQuery.isNotEmpty() -> districts.filter { district ->
                        district.name.contains(searchQuery, ignoreCase = true)
                    }

                    else -> districts
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    private val _sessionAR = MutableLiveData<Session?>()
    val sessionAR: LiveData<Session?> = _sessionAR

    private val _properties = MutableStateFlow<UIState<List<Property>>>(UIState.Loading())
    val properties = _properties.asStateFlow()

    private val _propertiesXOfferType =
        MutableStateFlow<UIState<List<PropertyHasOfferType>>>(UIState.Loading())
    val propertiesXOfferType = _propertiesXOfferType.asStateFlow()

    private val _devices = MutableStateFlow<UIState<List<Device>>>(UIState.Loading())
    val devices = _devices.asStateFlow()

    private val _images = MutableStateFlow<UIState<List<Image>>>(UIState.Loading())
    val images = _images.asStateFlow()

    private val _publishing = MutableStateFlow<UIState<List<Publishing>>>(UIState.Loading())
    val publishing = _publishing.asStateFlow()

    private val _publishingItems = MutableLiveData<Map<Int, Publishing>>()
    val publishingItems: LiveData<Map<Int, Publishing>> = _publishingItems

    private val _propertyItems = MutableLiveData<Map<Int, PropertyItem>>()
    val propertyItems: LiveData<Map<Int, PropertyItem>> = _propertyItems

    private val _insertRequest = MutableStateFlow<UIState<String>>(UIState.None())
    val insertRequest = _insertRequest.asStateFlow()

    private val _insertRequestXPublishing = MutableStateFlow<UIState<String>>(UIState.None())
    val insertRequestXPublishing = _insertRequestXPublishing.asStateFlow()

    private val _requestType = MutableLiveData<Int>(1)
    val requestType: LiveData<Int> = _requestType

    fun onLoadingVisible(visible: Boolean) {
        _loadingVisible.value = visible
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    fun updateFoundProperties(district: String) {
        foundProperties.entries.clear()
        onSearchQueryChange(district)
        foundProperties.putAll(
            propertyItems.value!!.toList().map { it.second }
                .filter { it.district.name == district }
                .map { Pair("${it.id}", it) }
        )
    }

    fun updateNearestProperties(current: LatLng, distance: Double) {
        nearbyProperties.entries.clear()
        nearbyProperties.putAll(
            propertyItems.value!!.toList().map { it.second }.filter {
                SphericalUtil.computeDistanceBetween(
                    current,
                    LatLng(it.location.latitude, it.location.longitude)
                ) <= distance
            }.map {
                Pair("${it.id}", AnchorItem(it))
            }
        )
    }

    fun timeTables() = arrayOf(
        "08:00 - 09:00",
        "09:00 - 10:00",
        "10:00 - 11:00",
        "11:00 - 12:00",
        "12:00 - 13:00",
        "13:00 - 14:00",
        "14:00 - 15:00",
        "15:00 - 16:00",
        "16:00 - 17:00",
        "17:00 - 18:00",
        "18:00 - 19:00",
        "19:00 - 20:00"
    )

    fun priotityTypes() = arrayOf(
        "Necesidad inmediata",
        "Quiero mudarme en un mes",
        "No tengo apuro en mudarme"
    )

    fun filterList() = listOf(
        FilterType.RESTAURANT,
        FilterType.EDUCATION,
        FilterType.SUPERMARKET,
        FilterType.MALL,
        FilterType.MARKET,
        FilterType.DRUGSTORE,
        FilterType.BAR
    )

    fun onVisibleChanged(visible: Boolean) {
        _bottomBarVisible.value = visible
    }

    fun onVisibleContactBarChanged(visible: Boolean) {
        _contactBottomBarVisible.value = visible
    }

    fun onTrackerMapChanged(visible: Boolean) {
        _trackerMapVisible.value = visible
    }

    fun onWhatsAppBottomSheetVisible(visible: Boolean) {
        _whatsappBottomSheetVisible.value = visible
    }

    fun onContactBottomSheetVisible(visible: Boolean) {
        _contactBottomSheetVisible.value = visible
    }

    fun onTrackerBottomSheetVisible(visible: Boolean) {
        _trackerBottomSheetVisible.value = visible
    }

    fun onTabIndexChanged(index: Int) {
        _tabIndex.value = index
    }

    fun onVisitDayDialogVisible(visible: Boolean) {
        _visitDayDialogVisible.value = visible
    }

    fun onConfirmDialogVisible(visible: Boolean) {
        _confirmDialogVisible.value = visible
    }

    fun onRequestDialogVisible(visible: Boolean) {
        _requestDialogVisible.value = visible
    }

    fun onErrorDialogVisible(visible: Boolean) {
        _errorDialogVisible.value = visible
    }

    fun onCompleteDialogVisible(visible: Boolean) {
        _completeDialogVisible.value = visible
    }

    fun onVisitDialogVisible(visible: Boolean) {
        _visitDialogVisible.value = visible
    }

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPhoneChanged(phone: String) {
        _phone.value = phone
    }

    fun onDniChanged(dni: String) {
        _dni.value = dni
    }

    fun onMessageChanged(message: String) {
        _message.value = message
    }

    fun onVisitDayChanged(visitDay: String) {
        _visitDay.value = visitDay
    }

    fun onVisitMillisChanged(visitMillis: Long) {
        _visitMillis.value = visitMillis
    }

    fun onVisitLocalChanged(visitLocal: LocalDateTime) {
        _visitLocal.value = visitLocal
    }

    fun onTimeTableChanged(timeTable: String) {
        _timeTable.value = timeTable
    }

    fun onPriorityTypeChanged(priorityType: String) {
        _priorityType.value = priorityType
    }

    fun onSessionARChanged(session: Session?) {
        _sessionAR.value = session
    }

    fun onPropertyItemsChanged(propertyItems: Map<Int, PropertyItem>) {
        _propertyItems.value = propertyItems
    }

    fun onDistrictChanged(districts: List<District>) {
        _districts.value = districts
    }

    fun onPublishingItemsChanged(publishingItems: Map<Int, Publishing>) {
        _publishingItems.value = publishingItems
    }

    fun onUserChanged(user: User) {
        _user.value = user
    }

    fun validateName(): Boolean {
        if (_name.value.isNullOrBlank()) {
            _nameMessageError.value = "Ingresa tu nombre completo"
            _nameError.value = true
        } else {
            _nameError.value = false
        }
        return _nameError.value == false
    }

    fun validateEmail(): Boolean {
        if (_email.value.isNullOrBlank()) {
            _emailMessageError.value = "Ingresa tu correo elentrónico"
            _emailError.value = true
        } else if (Patterns.EMAIL_ADDRESS.matcher(_email.value.toString()).matches().not()) {
            _emailMessageError.value = "Correo electrónico inválido"
            _emailError.value = true
        } else {
            _emailError.value = false
        }
        return _emailError.value == false
    }

    fun validatePhone(): Boolean {
        if (_phone.value.isNullOrBlank()) {
            _phoneMessageError.value = "Ingresa tu número telefónico"
            _phoneError.value = true
        } else if (_phone.value.toString().length < 9) {
            _phoneMessageError.value = "Número telefónico inválido"
            _phoneError.value = true
        } else {
            _phoneError.value = false
        }
        return _phoneError.value == false
    }

    fun validateDNI(): Boolean {
        if (_dni.value.isNullOrBlank()) {
            _dniMessageError.value = "Ingresa tu documento de identidad"
            _dniError.value = true
        } else if (_dni.value.toString().length < 8) {
            _dniMessageError.value = "Documento de indentidad inválido"
            _dniError.value = true
        } else {
            _dniError.value = false
        }
        return _dniError.value == false
    }

    fun validateMessage(): Boolean {
        if (_message.value.isNullOrBlank()) {
            _messageTextError.value = "Escribe tu mensaje"
            _messageError.value = true
        } else if (_message.value.toString().length > 2000) {
            _messageTextError.value = "Texto demasiado largo"
            _messageError.value = true
        } else {
            _messageError.value = false
        }
        return _messageError.value == false
    }

    fun validateForm(): Boolean {
        if (validateName() && validateEmail() && validatePhone() && validateDNI() && validateMessage()) {
            _requestType.postValue(1)
            return true
        } else {
            _errorDialogVisible.value = true
            return false
        }
    }

    fun validateWhatsApp(): Boolean {
        if (validatePhone() && validateMessage()) {
            _requestType.postValue(0)
            return true
        } else {
            _errorDialogVisible.value = true
            return false
        }
    }

    fun validateRequest(): Boolean {
        if (validateName() && validateEmail() && validatePhone() && validateDNI()) {
            _requestType.postValue(2)
            return true
        } else {
            _errorDialogVisible.value = true
            return false
        }

    }

    fun validateFormRefresh(): Boolean {
        if (validateName() && validateEmail() && validatePhone() && validateDNI() && validateMessage()) {
            _requestType.postValue(1)
            return true
        } else {
            return false
        }
    }

    fun validateRequestRefresh(): Boolean {
        if (validateName() && validateEmail() && validatePhone() && validateDNI()) {
            _requestType.postValue(2)
            return true
        } else {
            return false
        }

    }

    fun loadProperties() {
        viewModelScope.launch {
            repository.loadProperties()
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    _properties.value = UIState.Error(error = it)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 10000),
                    initialValue = UIState.Loading()
                ).collect { _properties.value = it }
        }
    }

    fun loadPropertiesHasOfferType() {
        viewModelScope.launch {
            repository.loadPropertyXOfferTypes()
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    _propertiesXOfferType.value = UIState.Error(error = it)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 10000),
                    initialValue = UIState.Loading()
                ).collect { _propertiesXOfferType.value = it }
        }
    }

    fun loadDevices() {
        viewModelScope.launch {
            repository.loadDevices()
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    _devices.value = UIState.Error(error = it)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 10000),
                    initialValue = UIState.Loading()
                ).collect { _devices.value = it }
        }
    }

    fun loadImages() {
        viewModelScope.launch {
            repository.loadImages()
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    _images.value = UIState.Error(error = it)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 10000),
                    initialValue = UIState.Loading()
                ).collect { _images.value = it }
        }
    }

    fun loadPublishing() {
        viewModelScope.launch {
            repository.loadPublishing()
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    _publishing.value = UIState.Error(error = it)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 10000),
                    initialValue = UIState.Loading()
                ).collect { _publishing.value = it }
        }
    }

    fun executeRequest(propertyItem: PropertyItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerRequest(request = getRequest(propertyItem = propertyItem))
        }
    }

    fun executeRequestXPublishing(id: Int, propertyItem: PropertyItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerRequestXPublishing(
                requestHasPublishing = RequestHasPublishing(
                    request = getRequest(id = id, propertyItem = propertyItem),
                    publishing = _publishingItems.value?.get(key = propertyItem.id)!!,
                    createDate = System.currentTimeMillis().millisToLocalDateTime()
                        .toKotlinLocalDateTime()
                )
            )
        }
    }

    fun registerRequest(request: Request) {
        viewModelScope.launch {
            repository.registerRequest(request = request)
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    _insertRequest.value = UIState.Error(error = it)
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(stopTimeoutMillis = 10000),
                    UIState.Loading()
                ).collect { _insertRequest.value = it }
        }
    }

    fun registerRequestXPublishing(requestHasPublishing: RequestHasPublishing) {
        viewModelScope.launch {
            repository.registerRequestXPublishing(requestHasPublishing = requestHasPublishing)
                .map { it }
                .flowOn(dispatcher)
                .catch {
                    _insertRequestXPublishing.value = UIState.Error(error = it)
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(stopTimeoutMillis = 10000),
                    UIState.Loading()
                ).collect { _insertRequestXPublishing.value = it }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRequest(id: Int = 0, propertyItem: PropertyItem) = Request(
        id = id,
        date = getVisitDate(),
        message = if (_requestType.value == 1) _message.value.toString()
        else "Solicitud de visita para el inmueble en ${propertyItem.district.name}.",
        requestType = if (_requestType.value == 2) RequestType(
            id = 2, name = "Visita"
        ) else RequestType(
            id = 1,
            name = "Consulta"
        ),
        requestState = RequestState(id = 1, name = "Solicitado"),
        user = _user.value!!
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getVisitDate(): LocalDateTime {
        val date = _visitMillis.value ?: System.currentTimeMillis()
        val index = timeTables().indexOf(_timeTable.value)
        return when (index) {
            0 -> date.plus(hourToMillis(hours = 8))
            1 -> date.plus(hourToMillis(hours = 9))
            2 -> date.plus(hourToMillis(hours = 10))
            3 -> date.plus(hourToMillis(hours = 11))
            4 -> date.plus(hourToMillis(hours = 12))
            5 -> date.plus(hourToMillis(hours = 13))
            6 -> date.plus(hourToMillis(hours = 14))
            7 -> date.plus(hourToMillis(hours = 15))
            8 -> date.plus(hourToMillis(hours = 16))
            9 -> date.plus(hourToMillis(hours = 17))
            10 -> date.plus(hourToMillis(hours = 18))
            11 -> date.plus(hourToMillis(hours = 19))
            else -> date.plus(hourToMillis(hours = 8))
        }.millisToLocalDateTime().toKotlinLocalDateTime()
    }

    fun clearForm() {
        onNameChanged("")
        onEmailChanged("")
        onPhoneChanged("")
        onDniChanged("")
        onMessageChanged("")
        onVisitDayChanged(today())
        onTimeTableChanged(timeTables()[0])
        onPriorityTypeChanged(priotityTypes()[0])
        _nameError.value = false
        _emailError.value = false
        _phoneError.value = false
        _dniError.value = false
        _messageError.value = false
    }
}
