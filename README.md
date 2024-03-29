# Takehome Assessment project template

This is a project template for AEM-based applications. It is intended as a best-practice set of examples as well as a potential starting point to develop your own functionality.

## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.content: contains content fragment model, graphQL query, content fragment and images
* all: a single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies

## Service Pack and Uber version

    Version - 6.5.19

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage
    
Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `core`)

    mvn clean install -PautoInstallPackage

## Endpoint to get Json

/bin/getGlobalNavigation.json/<content-fragment-path>

Example: /bin/getGlobalNavigation.json/content/dam/apple/content-fragment/header/global-header

## GraphQL query for Global Navigation

    https://github.com/RubenFernando/take-home-assessment/blob/master/ui.content/src/main/content/jcr_root/conf/apple/settings/graphql/persistentQueries/globalNavigation/_jcr_content/_jcr_data.binary

## Content Fragment Model

    https://github.com/RubenFernando/take-home-assessment/tree/master/ui.content/src/main/content/jcr_root/conf/apple/settings/dam/cfm/models

## Content Fragment

    https://github.com/RubenFernando/take-home-assessment/tree/master/ui.content/src/main/content/jcr_root/content/dam/apple/content-fragment

## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
