# SeatReservation

Application for making seat reservations in multiple offices in different locations. 


## Capabilities

Below funcionalities are provided within the application:

* Secured registration of users. Each user can change password, or reset it.
* Three user types: Administrator, User, Location Administrator with following utilities:

	* User:
		* default credentials: user, user
		<SCREENSHOT>
		* at home page, after logging in, can see his upcoming and recent reservations, and current restriction on seat availability.
		On the right side the newest notification for current user will be visible.
		<SCREENSHOT>
		* can make a reservation by clicking Reserve button on toolbar choosing Location, Building, Floor and Seat in selected period of time.
		<SCREENSHOT>
		* can see a detailed list of reservations by clickin on My reservations button on toolbar
		<SCREENSHOT>

	* Administrator:
		* default credentials: admin, admin
		* By clicking on Location Administration button on toolbar Administrator can: 
			* add/edit/delete: Location, Building, Door, Room, Seat, Equipement
			* add/delete: Restrictions
			* approve/delete: Pending reservations from users
			* add/approve/delete: all reservations
		* By clickintg on Administration button Administrator can:
			* activate/edit/delete users by clicking on User management
			<SCREENSHOT>
			* view various measurements from the application's operation

	* Location Administrator:
		* default credentials i.e.: krak, krak
		* can make a reservation for whole room, floor etc. (available in upcoming version)
		* have the same possibilities as Administrator in Location Administration, but limited to assigned location (available in upcoming version)
		* see only reservations made at his location

* Two languages to choose: Polish and English

## Known issues


## Build

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.


[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.9.2 archive]: https://www.jhipster.tech/documentation-archive/v7.9.2
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.9.2/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.9.2/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.9.2/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.9.2/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.9.2/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.9.2/setting-up-ci/
[node.js]: https://nodejs.org/
[npm]: https://www.npmjs.com/
[webpack]: https://webpack.github.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
[angular cli]: https://cli.angular.io/
