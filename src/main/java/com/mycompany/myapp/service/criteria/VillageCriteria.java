package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Village} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.VillageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /villages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class VillageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter villageId;

    private StringFilter villageCode;

    private StringFilter villageName;

    private StringFilter villageEnglishName;

    private LongFilter ruralDistrictId;

    private Boolean distinct;

    public VillageCriteria() {}

    public VillageCriteria(VillageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.villageId = other.villageId == null ? null : other.villageId.copy();
        this.villageCode = other.villageCode == null ? null : other.villageCode.copy();
        this.villageName = other.villageName == null ? null : other.villageName.copy();
        this.villageEnglishName = other.villageEnglishName == null ? null : other.villageEnglishName.copy();
        this.ruralDistrictId = other.ruralDistrictId == null ? null : other.ruralDistrictId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VillageCriteria copy() {
        return new VillageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getVillageId() {
        return villageId;
    }

    public IntegerFilter villageId() {
        if (villageId == null) {
            villageId = new IntegerFilter();
        }
        return villageId;
    }

    public void setVillageId(IntegerFilter villageId) {
        this.villageId = villageId;
    }

    public StringFilter getVillageCode() {
        return villageCode;
    }

    public StringFilter villageCode() {
        if (villageCode == null) {
            villageCode = new StringFilter();
        }
        return villageCode;
    }

    public void setVillageCode(StringFilter villageCode) {
        this.villageCode = villageCode;
    }

    public StringFilter getVillageName() {
        return villageName;
    }

    public StringFilter villageName() {
        if (villageName == null) {
            villageName = new StringFilter();
        }
        return villageName;
    }

    public void setVillageName(StringFilter villageName) {
        this.villageName = villageName;
    }

    public StringFilter getVillageEnglishName() {
        return villageEnglishName;
    }

    public StringFilter villageEnglishName() {
        if (villageEnglishName == null) {
            villageEnglishName = new StringFilter();
        }
        return villageEnglishName;
    }

    public void setVillageEnglishName(StringFilter villageEnglishName) {
        this.villageEnglishName = villageEnglishName;
    }

    public LongFilter getRuralDistrictId() {
        return ruralDistrictId;
    }

    public LongFilter ruralDistrictId() {
        if (ruralDistrictId == null) {
            ruralDistrictId = new LongFilter();
        }
        return ruralDistrictId;
    }

    public void setRuralDistrictId(LongFilter ruralDistrictId) {
        this.ruralDistrictId = ruralDistrictId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VillageCriteria that = (VillageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(villageId, that.villageId) &&
            Objects.equals(villageCode, that.villageCode) &&
            Objects.equals(villageName, that.villageName) &&
            Objects.equals(villageEnglishName, that.villageEnglishName) &&
            Objects.equals(ruralDistrictId, that.ruralDistrictId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, villageId, villageCode, villageName, villageEnglishName, ruralDistrictId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VillageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (villageId != null ? "villageId=" + villageId + ", " : "") +
            (villageCode != null ? "villageCode=" + villageCode + ", " : "") +
            (villageName != null ? "villageName=" + villageName + ", " : "") +
            (villageEnglishName != null ? "villageEnglishName=" + villageEnglishName + ", " : "") +
            (ruralDistrictId != null ? "ruralDistrictId=" + ruralDistrictId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
