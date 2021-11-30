docker run --restart always --name postgres13.2 --net dev-network -v /Users/flaviojuniordev/Documents/developer/postgres_data/13.2:/var/lib/postgresql/data -p 5432:5432 -d -e POSTGRES_PASSWORD=flaviojuniord3v postgres:13.2

