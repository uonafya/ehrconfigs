package org.openmrs.module.ehrconfigs.utils;

import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.List;

public class EhrConfigsUtils {
   public static Provider getProvider(Person person) {
        Provider provider = null;
        ProviderService providerService = Context.getProviderService();
        List<Provider> providerList = new ArrayList<Provider>(providerService.getProvidersByPerson(person));
        if(providerList.size() > 0){
            provider = providerList.get(0);
        }
        return provider;
    }
}
