package com.inmobixpress.inmobixpress.data.network.service

import com.inmobixpress.inmobixpress.data.network.model.Country
import com.inmobixpress.inmobixpress.data.network.model.Department
import com.inmobixpress.inmobixpress.data.network.model.Device
import com.inmobixpress.inmobixpress.data.network.model.District
import com.inmobixpress.inmobixpress.data.network.model.NetworkResult
import com.inmobixpress.inmobixpress.data.network.model.OfferType
import com.inmobixpress.inmobixpress.data.network.model.Property
import com.inmobixpress.inmobixpress.data.network.model.PropertyHasOfferType
import com.inmobixpress.inmobixpress.data.network.model.PropertyState
import com.inmobixpress.inmobixpress.data.network.model.PropertyType
import com.inmobixpress.inmobixpress.data.network.model.Province
import kotlinx.coroutines.flow.Flow

interface PropertyService {
    fun loadProperties(): Flow<NetworkResult<List<Property>>>
    fun loadProperty(id: Int): Flow<NetworkResult<Property>>
    fun registerProperty(property: Property): Flow<NetworkResult<String>>
    fun updateProperty(id: Int, property: Property): Flow<NetworkResult<String>>
    fun deleteProperty(id: Int): Flow<NetworkResult<String>>

    fun loadOfferTypes(): Flow<NetworkResult<List<OfferType>>>
    fun loadOfferType(id: Int): Flow<NetworkResult<OfferType>>
    fun registerOfferType(offerType: OfferType): Flow<NetworkResult<String>>
    fun updateOfferType(id: Int, offerType: OfferType): Flow<NetworkResult<String>>
    fun deleteOfferType(id: Int): Flow<NetworkResult<String>>

    fun loadPropertyXOfferTypes(): Flow<NetworkResult<List<PropertyHasOfferType>>>
    fun loadPropertyXOfferType(pId: Int, oId: Int): Flow<NetworkResult<PropertyHasOfferType>>
    fun registerPropertyXOfferType(
        propertyHasOfferType: PropertyHasOfferType,
    ): Flow<NetworkResult<String>>

    fun updatePropertyXOfferType(
        pId: Int,
        oId: Int,
        propertyHasOfferType: PropertyHasOfferType,
    ): Flow<NetworkResult<String>>

    fun deletePropertyXOfferType(pId: Int, oId: Int): Flow<NetworkResult<String>>

    fun loadPropertyTypes(): Flow<NetworkResult<List<PropertyType>>>
    fun loadPropertyType(id: Int): Flow<NetworkResult<PropertyType>>
    fun registerPropertyType(propertyType: PropertyType): Flow<NetworkResult<String>>
    fun updatePropertyType(id: Int, propertyType: PropertyType): Flow<NetworkResult<String>>
    fun deletePropertyType(id: Int): Flow<NetworkResult<String>>

    fun loadPropertyStates(): Flow<NetworkResult<List<PropertyState>>>
    fun loadPropertyState(id: Int): Flow<NetworkResult<PropertyState>>
    fun registerPropertyState(propertyState: PropertyState): Flow<NetworkResult<String>>
    fun updatePropertyState(id: Int, propertyState: PropertyState): Flow<NetworkResult<String>>
    fun deletePropertyState(id: Int): Flow<NetworkResult<String>>

    fun loadCountries(): Flow<NetworkResult<List<Country>>>
    fun loadCountry(id: Int): Flow<NetworkResult<Country>>
    fun registerCountry(country: Country): Flow<NetworkResult<String>>
    fun updateCountry(id: Int, country: Country): Flow<NetworkResult<String>>
    fun deleteCountry(id: Int): Flow<NetworkResult<String>>

    fun loadDepartments(): Flow<NetworkResult<List<Department>>>
    fun loadDepartment(id: Int): Flow<NetworkResult<Department>>
    fun registerDepartment(department: Department): Flow<NetworkResult<String>>
    fun updateDepartment(id: Int, department: Department): Flow<NetworkResult<String>>
    fun deleteDepartment(id: Int): Flow<NetworkResult<String>>

    fun loadProvinces(): Flow<NetworkResult<List<Province>>>
    fun loadProvince(id: Int): Flow<NetworkResult<Province>>
    fun registerProvince(province: Province): Flow<NetworkResult<String>>
    fun updateProvince(id: Int, province: Province): Flow<NetworkResult<String>>
    fun deleteProvince(id: Int): Flow<NetworkResult<String>>

    fun loadDistricts(): Flow<NetworkResult<List<District>>>
    fun loadDistrict(id: Int): Flow<NetworkResult<District>>
    fun registerDistrict(district: District): Flow<NetworkResult<String>>
    fun updateDistrict(id: Int, district: District): Flow<NetworkResult<String>>
    fun deleteDistrict(id: Int): Flow<NetworkResult<String>>

    fun loadDevices(): Flow<NetworkResult<List<Device>>>
    fun loadDevice(id: Int): Flow<NetworkResult<Device>>
    fun registerDevice(device: Device): Flow<NetworkResult<String>>
    fun updateDevice(id: Int, device: Device): Flow<NetworkResult<String>>
    fun deleteDevice(id: Int): Flow<NetworkResult<String>>
}