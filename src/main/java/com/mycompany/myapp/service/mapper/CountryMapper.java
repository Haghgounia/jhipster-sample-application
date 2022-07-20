package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Continent;
import com.mycompany.myapp.domain.Country;
import com.mycompany.myapp.domain.Currency;
import com.mycompany.myapp.domain.Language;
import com.mycompany.myapp.service.dto.ContinentDTO;
import com.mycompany.myapp.service.dto.CountryDTO;
import com.mycompany.myapp.service.dto.CurrencyDTO;
import com.mycompany.myapp.service.dto.LanguageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {
    @Mapping(target = "continent", source = "continent", qualifiedByName = "continentId")
    @Mapping(target = "currency", source = "currency", qualifiedByName = "currencyId")
    @Mapping(target = "officialLanguage", source = "officialLanguage", qualifiedByName = "languageId")
    CountryDTO toDto(Country s);

    @Named("continentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContinentDTO toDtoContinentId(Continent continent);

    @Named("currencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurrencyDTO toDtoCurrencyId(Currency currency);

    @Named("languageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LanguageDTO toDtoLanguageId(Language language);
}
