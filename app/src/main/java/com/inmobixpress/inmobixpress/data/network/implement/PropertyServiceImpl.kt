package com.inmobixpress.inmobixpress.data.network.implement

import com.inmobixpress.inmobixpress.data.network.model.Country
import com.inmobixpress.inmobixpress.data.network.model.Department
import com.inmobixpress.inmobixpress.data.network.model.Device
import com.inmobixpress.inmobixpress.data.network.model.District
import com.inmobixpress.inmobixpress.data.network.model.Image
import com.inmobixpress.inmobixpress.data.network.model.NetworkResult
import com.inmobixpress.inmobixpress.data.network.model.OfferType
import com.inmobixpress.inmobixpress.data.network.model.Property
import com.inmobixpress.inmobixpress.data.network.model.PropertyHasOfferType
import com.inmobixpress.inmobixpress.data.network.model.PropertyState
import com.inmobixpress.inmobixpress.data.network.model.PropertyType
import com.inmobixpress.inmobixpress.data.network.model.Province
import com.inmobixpress.inmobixpress.data.network.model.Publishing
import com.inmobixpress.inmobixpress.data.network.model.Request
import com.inmobixpress.inmobixpress.data.network.model.RequestHasPublishing
import com.inmobixpress.inmobixpress.data.network.service.PropertyService
import com.inmobixpress.inmobixpress.data.network.utils.toResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PropertyServiceImpl @Inject constructor(private val httpClient: HttpClient) :
    PropertyService {

    override fun loadProperties(): Flow<NetworkResult<List<Property>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/property").toResult<List<Property>>()
        emit(response)
    }

    override fun loadProperty(id: Int): Flow<NetworkResult<Property>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/property/{id?}") {
            parameter("id", id)
        }.toResult<Property>()
        emit(response)
    }

    override fun registerProperty(property: Property): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/property") {
            contentType(ContentType.Application.Json)
            setBody(property)
        }.toResult<String>()
        emit(response)
    }

    override fun updateProperty(id: Int, property: Property): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/property") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(property)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteProperty(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/property/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadOfferTypes(): Flow<NetworkResult<List<OfferType>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/offerType").toResult<List<OfferType>>()
        emit(response)
    }

    override fun loadOfferType(id: Int): Flow<NetworkResult<OfferType>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/offerType/{id?}") {
            parameter("id", id)
        }.toResult<OfferType>()
        emit(response)
    }

    override fun registerOfferType(offerType: OfferType): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/offerType") {
            contentType(ContentType.Application.Json)
            setBody(offerType)
        }.toResult<String>()
        emit(response)
    }

    override fun updateOfferType(id: Int, offerType: OfferType): Flow<NetworkResult<String>> =
        flow {
            emit(NetworkResult.Loading())
            val response = httpClient.put(urlString = "/offerType") {
                parameter("id", id)
                contentType(ContentType.Application.Json)
                setBody(offerType)
            }.toResult<String>()
            emit(response)
        }

    override fun deleteOfferType(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/offerType/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadPropertyXOfferTypes(): Flow<NetworkResult<List<PropertyHasOfferType>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/propertyHasOfferType")
            .toResult<List<PropertyHasOfferType>>()
        emit(response)
    }

    override fun loadPropertyXOfferType(
        pId: Int,
        oId: Int,
    ): Flow<NetworkResult<PropertyHasOfferType>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(
            urlString = "/propertyHasOfferType/{propertyId?}/{offerTypeId?}"
        ) {
            parameter("propertyId", pId)
            parameter("offerTypeId", oId)
        }.toResult<PropertyHasOfferType>()
        emit(response)
    }

    override fun registerPropertyXOfferType(
        propertyHasOfferType: PropertyHasOfferType,
    ): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/propertyHasOfferType") {
            contentType(ContentType.Application.Json)
            setBody(propertyHasOfferType)
        }.toResult<String>()
        emit(response)
    }

    override fun updatePropertyXOfferType(
        pId: Int,
        oId: Int,
        propertyHasOfferType: PropertyHasOfferType,
    ): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/propertyHasOfferType") {
            parameter("propertyId", pId)
            parameter("offerTypeId", oId)
            contentType(ContentType.Application.Json)
            setBody(propertyHasOfferType)
        }.toResult<String>()
        emit(response)
    }

    override fun deletePropertyXOfferType(pId: Int, oId: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/propertyHasOfferType/{propertyId?}/{offerTypeId?}") {
            parameter("propertyId", pId)
            parameter("offerTypeId", oId)
        }.toResult<String>()
        emit(response)
    }

    override fun loadPropertyTypes(): Flow<NetworkResult<List<PropertyType>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/propertyType").toResult<List<PropertyType>>()
        emit(response)
    }

    override fun loadPropertyType(id: Int): Flow<NetworkResult<PropertyType>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/propertyType/{id?}") {
            parameter("id", id)
        }.toResult<PropertyType>()
        emit(response)
    }

    override fun registerPropertyType(propertyType: PropertyType): Flow<NetworkResult<String>> =
        flow {
            emit(NetworkResult.Loading())
            val response = httpClient.post(urlString = "/propertyType") {
                contentType(ContentType.Application.Json)
                setBody(propertyType)
            }.toResult<String>()
            emit(response)
        }

    override fun updatePropertyType(
        id: Int,
        propertyType: PropertyType,
    ): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/propertyType") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(propertyType)
        }.toResult<String>()
        emit(response)
    }

    override fun deletePropertyType(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/propertyType/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadPropertyStates(): Flow<NetworkResult<List<PropertyState>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/propertyState").toResult<List<PropertyState>>()
        emit(response)
    }

    override fun loadPropertyState(id: Int): Flow<NetworkResult<PropertyState>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/propertyState/{id?}") {
            parameter("id", id)
        }.toResult<PropertyState>()
        emit(response)
    }

    override fun registerPropertyState(
        propertyState: PropertyState,
    ): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/propertyState") {
            contentType(ContentType.Application.Json)
            setBody(propertyState)
        }.toResult<String>()
        emit(response)
    }

    override fun updatePropertyState(
        id: Int,
        propertyState: PropertyState,
    ): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/propertyState") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(propertyState)
        }.toResult<String>()
        emit(response)
    }

    override fun deletePropertyState(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/propertyState/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadCountries(): Flow<NetworkResult<List<Country>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/country").toResult<List<Country>>()
        emit(response)
    }

    override fun loadCountry(id: Int): Flow<NetworkResult<Country>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/country/{id?}") {
            parameter("id", id)
        }.toResult<Country>()
        emit(response)
    }

    override fun registerCountry(country: Country): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/country") {
            contentType(ContentType.Application.Json)
            setBody(country)
        }.toResult<String>()
        emit(response)
    }

    override fun updateCountry(id: Int, country: Country): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/country") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(country)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteCountry(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/country/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadDepartments(): Flow<NetworkResult<List<Department>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/department").toResult<List<Department>>()
        emit(response)
    }

    override fun loadDepartment(id: Int): Flow<NetworkResult<Department>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/department/{id?}") {
            parameter("id", id)
        }.toResult<Department>()
        emit(response)
    }

    override fun registerDepartment(department: Department): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/department") {
            contentType(ContentType.Application.Json)
            setBody(department)
        }.toResult<String>()
        emit(response)
    }

    override fun updateDepartment(id: Int, department: Department): Flow<NetworkResult<String>> =
        flow {
            emit(NetworkResult.Loading())
            val response = httpClient.put(urlString = "/department") {
                parameter("id", id)
                contentType(ContentType.Application.Json)
                setBody(department)
            }.toResult<String>()
            emit(response)
        }

    override fun deleteDepartment(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/department/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadProvinces(): Flow<NetworkResult<List<Province>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/province").toResult<List<Province>>()
        emit(response)
    }

    override fun loadProvince(id: Int): Flow<NetworkResult<Province>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/province/{id?}") {
            parameter("id", id)
        }.toResult<Province>()
        emit(response)
    }

    override fun registerProvince(province: Province): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/province") {
            contentType(ContentType.Application.Json)
            setBody(province)
        }.toResult<String>()
        emit(response)
    }

    override fun updateProvince(id: Int, province: Province): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/province") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(province)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteProvince(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/province/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadDistricts(): Flow<NetworkResult<List<District>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/district").toResult<List<District>>()
        emit(response)
    }

    override fun loadDistrict(id: Int): Flow<NetworkResult<District>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/district/{id?}") {
            parameter("id", id)
        }.toResult<District>()
        emit(response)
    }

    override fun registerDistrict(district: District): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/district") {
            contentType(ContentType.Application.Json)
            setBody(district)
        }.toResult<String>()
        emit(response)
    }

    override fun updateDistrict(id: Int, district: District): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/district") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(district)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteDistrict(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/district/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadDevices(): Flow<NetworkResult<List<Device>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/device").toResult<List<Device>>()
        emit(response)
    }

    override fun loadDevice(id: Int): Flow<NetworkResult<Device>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/device/{id?}") {
            parameter("id", id)
        }.toResult<Device>()
        emit(response)
    }

    override fun registerDevice(device: Device): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/device") {
            contentType(ContentType.Application.Json)
            setBody(device)
        }.toResult<String>()
        emit(response)
    }

    override fun updateDevice(id: Int, device: Device): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/device") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(device)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteDevice(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/device/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadImages(): Flow<NetworkResult<List<Image>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/image").toResult<List<Image>>()
        emit(response)
    }

    override fun loadImage(id: Int): Flow<NetworkResult<Image>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/image/{id?}") {
            parameter("id", id)
        }.toResult<Image>()
        emit(response)
    }

    override fun registerImage(image: Image): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/image") {
            contentType(ContentType.Application.Json)
            setBody(image)
        }.toResult<String>()
        emit(response)
    }

    override fun updateImage(id: Int, image: Image): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/image") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(image)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteImage(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/image/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadRequests(): Flow<NetworkResult<List<Request>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/request").toResult<List<Request>>()
        emit(response)
    }

    override fun loadRequest(id: Int): Flow<NetworkResult<Request>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/request/{id?}") {
            parameter("id", id)
        }.toResult<Request>()
        emit(response)
    }

    override fun registerRequest(request: Request): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/request") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.toResult<String>()
        emit(response)
    }

    override fun updateRequest(id: Int, request: Request): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/request") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteRequest(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/request/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadPublishing(): Flow<NetworkResult<List<Publishing>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/publishing").toResult<List<Publishing>>()
        emit(response)
    }

    override fun loadPublishing(id: Int): Flow<NetworkResult<Publishing>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/publishing/{id?}") {
            parameter("id", id)
        }.toResult<Publishing>()
        emit(response)
    }

    override fun registerPublishing(publishing: Publishing): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/publishing") {
            contentType(ContentType.Application.Json)
            setBody(publishing)
        }.toResult<String>()
        emit(response)
    }

    override fun updatePublishing(id: Int, publishing: Publishing): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/publishing") {
            parameter("id", id)
            contentType(ContentType.Application.Json)
            setBody(publishing)
        }.toResult<String>()
        emit(response)
    }

    override fun deletePublishing(id: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/publishing/{id?}") {
            parameter("id", id)
        }.toResult<String>()
        emit(response)
    }

    override fun loadRequestsXPublishing(): Flow<NetworkResult<List<RequestHasPublishing>>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(urlString = "/requestHasPublishing")
            .toResult<List<RequestHasPublishing>>()
        emit(response)
    }

    override fun loadRequestXPublishing(
        rId: Int,
        pId: Int
    ): Flow<NetworkResult<RequestHasPublishing>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.get(
            urlString = "/requestHasPublishing/{requestId?}/{publishingId?}"
        ) {
            parameter("requestId", rId)
            parameter("publishingId", pId)
        }.toResult<RequestHasPublishing>()
        emit(response)
    }

    override fun registerRequestXPublishing(requestHasPublishing: RequestHasPublishing): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.post(urlString = "/requestHasPublishing") {
            contentType(ContentType.Application.Json)
            setBody(requestHasPublishing)
        }.toResult<String>()
        emit(response)
    }

    override fun updateRequestXPublishing(
        rId: Int,
        pId: Int,
        requestHasPublishing: RequestHasPublishing
    ): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.put(urlString = "/requestHasPublishing") {
            parameter("requestId", rId)
            parameter("publishingId", pId)
            contentType(ContentType.Application.Json)
            setBody(requestHasPublishing)
        }.toResult<String>()
        emit(response)
    }

    override fun deleteRequestXPublishing(rId: Int, pId: Int): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading())
        val response = httpClient.delete(urlString = "/requestHasPublishing/{requestId?}/{publishingId?}") {
            parameter("requestId", rId)
            parameter("publishingId", pId)
        }.toResult<String>()
        emit(response)
    }
}