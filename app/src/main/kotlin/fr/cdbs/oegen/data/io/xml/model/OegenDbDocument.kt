package fr.cdbs.oegen.data.io.xml.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import fr.cdbs.oegen.data.io.xml.XmlConstants

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = XmlConstants.DB_NAME, namespace = XmlConstants.DB_NAMESPACE)
data class OegenDbDocument(
    @get:JacksonXmlProperty(isAttribute = true, namespace = XmlConstants.W3C_INSTANCE_NS, localName = "schemaLocation")
    val schemaLocation: String = XmlConstants.SCHEMA_LOCATION,
    @get:JacksonXmlElementWrapper(useWrapping = false)
    @get:JacksonXmlProperty(localName = XmlConstants.PERSON_NAME, namespace = XmlConstants.DB_NAMESPACE)
    val persons: List<PersonNode> = emptyList(),
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class PersonNode(
    @get:JacksonXmlProperty(isAttribute = true, localName = XmlConstants.PERSON_ID)
    val id: String? = null,
    @get:JacksonXmlProperty(isAttribute = true, localName = XmlConstants.PERSON_ATTR_RELATION)
    val relation: String? = null,
    @get:JacksonXmlProperty(localName = XmlConstants.PERSON_FIRSTNAME, namespace = XmlConstants.DB_NAMESPACE)
    val firstName: String? = null,
    @get:JacksonXmlProperty(localName = XmlConstants.PERSON_LASTNAME, namespace = XmlConstants.DB_NAMESPACE)
    val lastName: String? = null,
    @get:JacksonXmlProperty(localName = XmlConstants.PERSON_GENDER, namespace = XmlConstants.DB_NAMESPACE)
    val gender: String? = null,
    @get:JacksonXmlElementWrapper(useWrapping = false)
    @get:JacksonXmlProperty(localName = XmlConstants.PERSON_NAME, namespace = XmlConstants.DB_NAMESPACE)
    val nestedPersons: List<PersonNode>? = null,
    @get:JacksonXmlProperty(localName = XmlConstants.LISTOF_NAME, namespace = XmlConstants.DB_NAMESPACE)
    val lists: List<RelationListNode>? = null,
    @get:JacksonXmlProperty(localName = XmlConstants.PERSONREF_NAME, namespace = XmlConstants.DB_NAMESPACE)
    val personRefs: List<PersonRefNode>? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class RelationListNode(
    @get:JacksonXmlProperty(isAttribute = true, localName = XmlConstants.LISTOF_ATTR_OF)
    val of: String = "",
    @get:JacksonXmlElementWrapper(useWrapping = false)
    @get:JacksonXmlProperty(localName = XmlConstants.PERSON_NAME, namespace = XmlConstants.DB_NAMESPACE)
    val persons: List<PersonNode>? = null,
    @get:JacksonXmlProperty(localName = XmlConstants.PERSONREF_NAME, namespace = XmlConstants.DB_NAMESPACE)
    val personRefs: List<PersonRefNode>? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class PersonRefNode(
    @get:JacksonXmlProperty(isAttribute = true, localName = XmlConstants.PERSONREF_ATTR_REF)
    val ref: String = "",
    @get:JacksonXmlProperty(isAttribute = true, localName = XmlConstants.LISTOF_ATTR_OF)
    val of: String? = null,
)
