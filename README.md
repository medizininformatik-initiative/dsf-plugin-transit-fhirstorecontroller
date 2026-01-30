# Fhir Store Controller Processes

Business processes for the Fhir Store Controller of MII Transit project as plugins for the [Data Sharing Framework][1].

## Usage

For documentation of deployment and configuration of the Fhir Store Controller see the wiki[2].

## Development

### Build

Prerequisite: Java 17, Maven >= 3.6

Build the project from the root directory of this repository by executing the following command.

```sh
mvn clean package
```

### Testing

For running the Fhir Store Controller processes you need a working and up-to-date DSF instance by following the
[DSF installation guide][3]. The processes are deployed to the DSF BPE component by adding the JAR file of the latest
stable release of the feasibility process plugin to your `process` folder of the DSF BPE. The processes have to be
configured as outlined in the configuration section in the [store controller overview][2].

### Update

For updating the store controller processes to the latest stable version do the following steps:

* stop your running DSF BPE instance
* replace the existing JAR file of the store controller processes plugin in your `process` folder of you DSF BPE with the JAR
  file of the latest stable release of the store controller process plugin
* compare and adjust your existing processes configuration to the configuration options documented in the
  [store controller process overview][2]

### Edit

You should edit the *.bpmn files only with the standalone [Camunda Modeler][4], because of different
formatting of the bpmn tools and plugins.

## License

This project is released under the terms of [GPL version 3](https://www.gnu.org/licenses/agpl-3.0.html).

```
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```

[1]: <https://dsf.dev>
[2]: <https://github.com/medizininformatik-initiative/dsf-plugin-transit-fhirstorecontroller/wiki>
[3]: <https://dsf.dev/stable/maintain/install.html>
[4]: <https://camunda.com/download/modeler/>
