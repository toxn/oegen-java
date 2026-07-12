package fr.cdbs.oegen.data.io.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import fr.cdbs.oegen.data.date.FuzzyDate
import fr.cdbs.oegen.data.io.IoConstants

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonRootName(IoConstants.DB_NAME)
@JacksonXmlRootElement(localName = IoConstants.DB_NAME, namespace = IoConstants.DB_NAMESPACE)
data class OegenDbDocument(
    @get:JsonProperty("schemaLocation")
    @get:JacksonXmlProperty(isAttribute = true, namespace = IoConstants.W3C_INSTANCE_NS, localName = "schemaLocation")
    val schemaLocation: String = IoConstants.SCHEMA_LOCATION,
    @get:JsonProperty("persons")
    @get:JacksonXmlElementWrapper(useWrapping = false)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_NAME, namespace = IoConstants.DB_NAMESPACE)
    val persons: List<PersonNode> = emptyList(),
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class PersonNode(
    @get:JsonProperty(IoConstants.PERSON_ID)
    @get:JacksonXmlProperty(isAttribute = true, localName = IoConstants.PERSON_ID)
    val id: String? = null,
    @get:JsonProperty(IoConstants.PERSON_ATTR_RELATION)
    @get:JacksonXmlProperty(isAttribute = true, localName = IoConstants.PERSON_ATTR_RELATION)
    val relation: String? = null,
    @get:JsonProperty(IoConstants.PERSON_FIRSTNAME)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_FIRSTNAME, namespace = IoConstants.DB_NAMESPACE)
    val firstName: String? = null,
    @get:JsonProperty(IoConstants.PERSON_LASTNAME)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_LASTNAME, namespace = IoConstants.DB_NAMESPACE)
    val lastName: String? = null,
    @get:JsonProperty(IoConstants.PERSON_GENDER)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_GENDER, namespace = IoConstants.DB_NAMESPACE)
    val gender: String? = null,

    @get:JsonProperty(IoConstants.PERSON_BIRTH)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_BIRTH, namespace = IoConstants.DB_NAMESPACE)
    val birth: FuzzyDate? = null,
    @get:JsonProperty(IoConstants.PERSON_BAPTISM)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_BAPTISM, namespace = IoConstants.DB_NAMESPACE)
    val baptism: FuzzyDate? = null,
    @get:JsonProperty(IoConstants.PERSON_MARRIAGE)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_MARRIAGE, namespace = IoConstants.DB_NAMESPACE)
    val marriage: FuzzyDate? = null,
    @get:JsonProperty(IoConstants.PERSON_DEATH)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_DEATH, namespace = IoConstants.DB_NAMESPACE)
    val death: FuzzyDate? = null,
    @get:JsonProperty(IoConstants.PERSON_FUNERAL)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_FUNERAL, namespace = IoConstants.DB_NAMESPACE)
    val funeral: FuzzyDate? = null,
    @get:JsonProperty(IoConstants.PERSON_BIRTH_PLACE)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_BIRTH_PLACE, namespace = IoConstants.DB_NAMESPACE)
    val birthPlace: String? = null,
    @get:JsonProperty(IoConstants.PERSON_BAPTISM_PLACE)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_BAPTISM_PLACE, namespace = IoConstants.DB_NAMESPACE)
    val baptismPlace: String? = null,
    @get:JsonProperty(IoConstants.PERSON_MARRIAGE_PLACE)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_MARRIAGE_PLACE, namespace = IoConstants.DB_NAMESPACE)
    val marriagePlace: String? = null,
    @get:JsonProperty(IoConstants.PERSON_DEATH_PLACE)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_DEATH_PLACE, namespace = IoConstants.DB_NAMESPACE)
    val deathPlace: String? = null,
    @get:JsonProperty(IoConstants.PERSON_FUNERAL_PLACE)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_FUNERAL_PLACE, namespace = IoConstants.DB_NAMESPACE)
    val funeralPlace: String? = null,
    @get:JsonProperty("alternateNames")
    @get:JacksonXmlElementWrapper(useWrapping = false)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_ALTERNATE_NAME, namespace = IoConstants.DB_NAMESPACE)
    val alternateNames: List<AlternateNameNode>? = null,
    @get:JsonProperty("nestedPersons")
    @get:JacksonXmlElementWrapper(useWrapping = false)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_NAME, namespace = IoConstants.DB_NAMESPACE)
    val nestedPersons: List<PersonNode>? = null,
    @get:JsonProperty("lists")
    @get:JacksonXmlProperty(localName = IoConstants.LISTOF_NAME, namespace = IoConstants.DB_NAMESPACE)
    val lists: List<RelationListNode>? = null,
    @get:JsonProperty("personRefs")
    @get:JacksonXmlProperty(localName = IoConstants.PERSONREF_NAME, namespace = IoConstants.DB_NAMESPACE)
    val personRefs: List<PersonRefNode>? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class RelationListNode(
    @get:JsonProperty(IoConstants.LISTOF_ATTR_OF)
    @get:JacksonXmlProperty(isAttribute = true, localName = IoConstants.LISTOF_ATTR_OF)
    val of: String = "",
    @get:JsonProperty("persons")
    @get:JacksonXmlElementWrapper(useWrapping = false)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_NAME, namespace = IoConstants.DB_NAMESPACE)
    val persons: List<PersonNode>? = null,
    @get:JsonProperty("personRefs")
    @get:JacksonXmlProperty(localName = IoConstants.PERSONREF_NAME, namespace = IoConstants.DB_NAMESPACE)
    val personRefs: List<PersonRefNode>? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class PersonRefNode(
    @get:JsonProperty(IoConstants.PERSONREF_ATTR_REF)
    @get:JacksonXmlProperty(isAttribute = true, localName = IoConstants.PERSONREF_ATTR_REF)
    val ref: String = "",
    @get:JsonProperty(IoConstants.LISTOF_ATTR_OF)
    @get:JacksonXmlProperty(isAttribute = true, localName = IoConstants.LISTOF_ATTR_OF)
    val of: String? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class AlternateNameNode(
    @get:JsonProperty(IoConstants.PERSON_ALTERNATE_KIND)
    @get:JacksonXmlProperty(isAttribute = true, localName = IoConstants.PERSON_ALTERNATE_KIND)
    val kind: String? = null,
    @get:JsonProperty(IoConstants.PERSON_FIRSTNAME)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_FIRSTNAME, namespace = IoConstants.DB_NAMESPACE)
    val firstName: String? = null,
    @get:JsonProperty(IoConstants.PERSON_LASTNAME)
    @get:JacksonXmlProperty(localName = IoConstants.PERSON_LASTNAME, namespace = IoConstants.DB_NAMESPACE)
    val lastName: String? = null,
)
