package com.inmobixpress.inmobixpress.repository

import android.util.Log
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
import com.inmobixpress.inmobixpress.data.network.service.PropertyService
import com.inmobixpress.inmobixpress.ui.model.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PropertyRepository @Inject constructor(private val propertyService: PropertyService) {

    fun loadProperties(): Flow<UIState<List<Property>>> {
        return propertyService.loadProperties().map { result ->
            Log.e("REP", result.toString())
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadProperty(id: Int): Flow<UIState<Property>> {
        return propertyService.loadProperty(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerProperty(property: Property): Flow<UIState<String>> {
        return propertyService.registerProperty(property = property).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updateProperty(id: Int, property: Property): Flow<UIState<String>> {
        return propertyService.updateProperty(id = id, property = property).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deleteProperty(id: Int): Flow<UIState<String>> {
        return propertyService.deleteProperty(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadOfferTypes(): Flow<UIState<List<OfferType>>> {
        return propertyService.loadOfferTypes().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadOfferType(id: Int): Flow<UIState<OfferType>> {
        return propertyService.loadOfferType(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerOfferType(offerType: OfferType): Flow<UIState<String>> {
        return propertyService.registerOfferType(offerType = offerType).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updateOfferType(id: Int, offerType: OfferType): Flow<UIState<String>> {
        return propertyService.updateOfferType(id = id, offerType = offerType).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deleteOfferType(id: Int): Flow<UIState<String>> {
        return propertyService.deleteOfferType(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadPropertyXOfferTypes(): Flow<UIState<List<PropertyHasOfferType>>> {
        return propertyService.loadPropertyXOfferTypes().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadPropertyXOfferType(pId: Int, oId: Int): Flow<UIState<PropertyHasOfferType>> {
        return propertyService.loadPropertyXOfferType(pId = pId, oId = oId).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerPropertyXOfferType(
        propertyHasOfferType: PropertyHasOfferType,
    ): Flow<UIState<String>> {
        return propertyService.registerPropertyXOfferType(
            propertyHasOfferType = propertyHasOfferType
        ).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updatePropertyXOfferType(
        pId: Int,
        oId: Int,
        propertyHasOfferType: PropertyHasOfferType,
    ): Flow<UIState<String>> {
        return propertyService.updatePropertyXOfferType(
            pId = pId, oId = oId, propertyHasOfferType = propertyHasOfferType
        ).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deletePropertyXOfferType(pId: Int, oId: Int): Flow<UIState<String>> {
        return propertyService.deletePropertyXOfferType(pId = pId, oId = oId).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadPropertyTypes(): Flow<UIState<List<PropertyType>>> {
        return propertyService.loadPropertyTypes().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadPropertyType(id: Int): Flow<UIState<PropertyType>> {
        return propertyService.loadPropertyType(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerPropertyType(propertyType: PropertyType): Flow<UIState<String>> {
        return propertyService.registerPropertyType(propertyType = propertyType).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updatePropertyType(id: Int, propertyType: PropertyType): Flow<UIState<String>> {
        return propertyService.updatePropertyType(
            id = id, propertyType = propertyType
        ).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deletePropertyType(id: Int): Flow<UIState<String>> {
        return propertyService.deletePropertyType(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadPropertyStates(): Flow<UIState<List<PropertyState>>> {
        return propertyService.loadPropertyStates().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadPropertyState(id: Int): Flow<UIState<PropertyState>> {
        return propertyService.loadPropertyState(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerPropertyState(propertyState: PropertyState): Flow<UIState<String>> {
        return propertyService.registerPropertyState(propertyState = propertyState).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updatePropertyState(id: Int, propertyState: PropertyState): Flow<UIState<String>> {
        return propertyService.updatePropertyState(
            id = id, propertyState = propertyState
        ).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deletePropertyState(id: Int): Flow<UIState<String>> {
        return propertyService.deletePropertyState(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadCountries(): Flow<UIState<List<Country>>> {
        return propertyService.loadCountries().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadCountry(id: Int): Flow<UIState<Country>> {
        return propertyService.loadCountry(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerCountry(country: Country): Flow<UIState<String>> {
        return propertyService.registerCountry(country = country).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updateCountry(id: Int, country: Country): Flow<UIState<String>> {
        return propertyService.updateCountry(id = id, country = country).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deleteCountry(id: Int): Flow<UIState<String>> {
        return propertyService.deleteCountry(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadDepartments(): Flow<UIState<List<Department>>> {
        return propertyService.loadDepartments().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadDepartment(id: Int): Flow<UIState<Department>> {
        return propertyService.loadDepartment(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerDepartment(department: Department): Flow<UIState<String>> {
        return propertyService.registerDepartment(department = department).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updateDepartment(id: Int, department: Department): Flow<UIState<String>> {
        return propertyService.updateDepartment(id = id, department = department).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deleteDepartment(id: Int): Flow<UIState<String>> {
        return propertyService.deleteDepartment(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadProvinces(): Flow<UIState<List<Province>>> {
        return propertyService.loadProvinces().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadProvince(id: Int): Flow<UIState<Province>> {
        return propertyService.loadProvince(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerProvince(province: Province): Flow<UIState<String>> {
        return propertyService.registerProvince(province = province).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updateProvince(id: Int, province: Province): Flow<UIState<String>> {
        return propertyService.updateProvince(id = id, province = province).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deleteProvince(id: Int): Flow<UIState<String>> {
        return propertyService.deleteProvince(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadDistricts(): Flow<UIState<List<District>>> {
        return propertyService.loadDistricts().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadDistrict(id: Int): Flow<UIState<District>> {
        return propertyService.loadDistrict(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun registerDistrict(district: District): Flow<UIState<String>> {
        return propertyService.registerDistrict(district = district).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun updateDistrict(id: Int, district: District): Flow<UIState<String>> {
        return propertyService.updateDistrict(id = id, district = district).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun deleteDistrict(id: Int): Flow<UIState<String>> {
        return propertyService.deleteDistrict(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }

    fun loadDevices(): Flow<UIState<List<Device>>> {
        return propertyService.loadDevices().map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }
    fun loadDevice(id: Int): Flow<UIState<Device>> {
        return propertyService.loadDevice(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }
    fun registerDevice(device: Device): Flow<UIState<String>> {
        return propertyService.registerDevice(device = device).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }
    fun updateDevice(id: Int, device: Device): Flow<UIState<String>> {
        return propertyService.updateDevice(id = id, device = device).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }
    fun deleteDevice(id: Int): Flow<UIState<String>> {
        return propertyService.deleteDevice(id = id).map { result ->
            when (result) {
                is NetworkResult.Success -> UIState.Success(result.data)
                is NetworkResult.Loading -> UIState.Loading()
                is NetworkResult.Error -> UIState.Error(result.error)
            }
        }
    }
}