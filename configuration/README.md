# OpenMRS Initializer module configurations
### Introduction
The Initializer module is an API-only module that processes the content of the **configuration** folder when it is found inside OpenMRS' application data directory:

<pre>
.
├── modules/
├── openmrs.war
├── openmrs-runtime.properties
├── ...
└── <b>configuration/</b>
</pre>
The configuration folder is subdivided into _domain_ subfolders:
```bash
configuration/
  ├── addresshierarchy/
  ├── ampathforms/
  ├── appointmentspecialities/
  ├── appointmentservicedefinitions/
  ├── appointmentservicetypes/
  ├── attributetypes/
  ├── autogenerationoptions/
  ├── bahmniforms/
  ├── conceptclasses/
  ├── conceptsources/
  ├── concepts/
  ├── conceptsets/
  ├── datafiltermappings/
  ├── drugs/
  ├── encountertypes/
  ├── globalproperties/
  ├── htmlforms/
  ├── idgen/
  ├── jsonkeyvalues/
  ├── locations/
  ├── locationtagmaps/
  ├── locationtags/
  ├── messageproperties/
  ├── metadatasetmembers/ 
  ├── metadatasets/ 
  ├── metadatasharing/ 
  ├── metadatatermmappings/
  ├── ocl/
  ├── orderfrequencies/
  ├── ordertypes/
  ├── patientidentifiertypes/ 
  ├── personattributetypes/ 
  ├── privileges/ 
  ├── programs/ 
  ├── programworkflows/
  ├── programworkflowstates/
  ├── providerroles/ 
  ├── relationshiptypes/
  └── roles/
   
```  
Each domain-specific subfolder contains OpenMRS metadata configuration files that pertains to the domain.
