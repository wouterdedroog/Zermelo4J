# Zermelo4J
A simple Java API Wrapper for the Zermelo API

## Documentation
https://docs.mrwouter.nl/zermelo4j/


## Using Zermelo4J with Maven.
Add this repository
```
<repository>
   <id>mrwouter-repo</id>
   <url>https://repo.mrwouter.nl</url>
</repository> 
```
And this dependency
```
<dependency>
   <groupId>nl.mrwouter</groupId>
   <artifactId>Zermelo4J</artifactId>
   <version>1.3</version>
</dependency>
```


## Example
```
	public static void main(String args[]) {
		// Access token can be created by using ZermeloAPI#getAccessToken("[school]", "[koppel app code]");
		ZermeloAPI api = ZermeloAPI.getAPI("[school]", "[AccessToken]");
		System.out.println(api.getAccessToken());
		Date endDate = new Date();
		endDate.setTime(endDate.getTime() - 432000000l);
		
		for (Appointment app: api.getAppointments(endDate, new Date())) {
			System.out.println(app.toString());
		}
		for (Announcement ann: api.getAnnouncements()) {
			System.out.println(ann.toString());
		}
	}
```

## Disclaimer
This project is not created by nor associated in any way with Zermelo Software BV.
