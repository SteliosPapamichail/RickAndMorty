# Architecture
Since the project is small I tried to achieve a middle ground between a "proper" clean architecture approach (with data/domain/app modules) and a pragmatic approach.
For this reason, I kept the `app` module and instead used packages to structure and organize my code. Doing it this way has of course some downsides like proper separation as well as 
losing out on useful visibility modifiers like `internal` for certain classes but I find it pragmatic for something this small : )

## Data
For the `data` layer, I have added the concrete implementations of the abstract types found in the `domain` module, such as Repository implementations & Exporter(s), as well as platform specific classes and business logic. Such examples are the Retrofit client, the api interfaces, database-specific classes, models & daos, and also any relevant DTOs. This layer also holds the main `RemoteMediator` used via the Pagin3 library for the Episodes screen (see next sections for more details).

## Domain
As for the `domain` layer, I've kept it as close as possible to a pure Kotlin "module", by only including pure domain models that the UI will receive, abstract contracts for things such as repositories and exporters, custom exceptions and of course any use-cases.

## UI
Finally, for the UI I decided to go with an MVI variation that seems to be accepted in the Compose world. Specifically, it's a combination of MVVM and UDF where the ViewModels expose either one or two flows, one `StateFlow<T>` for the actual `@Immutable` UI state and optionally a second `SharedFlow<T>` for emitting one time events, such as non-breaking errors that would be nice for the user to receive (via a Toast for example).

This way, data flows downstream via the flows to the UI, which observes them and reacts to them, while any "events" are emulated via calling ViewModel functions (see export functionality).

# Persistence
My implementation follows the single source of truth principle by leveraging a local Room database which is the source of all data that's sent downstream. I'll break the logic down on a per-screen level due to the differences between screens requiring paging and sreens that do not.

## Episodes List
The `EpisodeRepository` implementation (see `fun getAllEpisodes(filters: Map<String, String>): PagingSource<Int, EpisodePreviewEntity>`) simply returns the `PagingSource` that Rooms provides while all the magic, happens in the `EpisodeRemoteMediator`. 

This class is a subclass of Pagin3's `RemoteMediator` and is reponsible for determining when to fetch more paged data and how. I'll avoid explaining the basics of the library itself, such as the `LoadType` since the documentation covers this. To dive a bit deeper in the specifics now, based on the aforementioned `LoadType`, I determine whether I should fully refresh the list (i.e. the user has used the pull-to-refresh gesture) or simply attempt to fetch the next page if available.

When appending a potential next page, I retrieve the last loaded item in the list and retrieve the page information based on that item's page via the respective DAO. Once those details have been fetched (if available, otherwise see the various `return MediatorResult` statements), the retrieval of the next page is executed via the remote api. This retrieval is straightforward, it requests the next page from the API, processes it in order to extract the `<List<Episode>>` and the pagination info if possible and finally, attempts to persist all that information in the database.

For the peristence, a single transaction is used via `db.withTransaction` in order to guarantee integrity and consistence for the data. Within this transaction, a timestamp is also created/updated through DataStore Preferences.

## Episode/Character Details

For the rest of the data, the logic is similar but implemented within the respective repository implementations. Let's look at the `EpisodeDetails` as an example, the same applies for the character as well :)

The `suspend fun getEpisodeDetailsAsFlow(episodeId: Int): Flow<Resource<EpisodeDetails>>` function exposes a `Flow<T>` that the UI can directly react to. The `Resource<out T>` is a helper wrapper class so that retrieval states can be modeled and emitted through the flow. Into the specifics now! Initially, a loading state is emitted and the current `EpisodeDetailsEntity?` is retrieved from the database so that a TTL check can be performed. If the data exists and is fresh, then it's emitted downstream and the flow concludes. If the lookup was a miss or if the data is stale, an API request is made and then depending on whether the request was successful, the response is persisted in the database along with the updated TTL and sent downstream.

# Pagination
For this feature, I actually took a small risk by using the Pagin3 library for the first time :) Usually I would manage the paging manually but opted for a tried and tested first-party library by Google for a change and to also learn something new along the way!

For this reason, the `EpisodeDao` exposes `fun pagingSource(): PagingSource<Int, EpisodePreviewEntity>` as the `PagingSource` and the library handles the observing and notifying of the subscriber for any changes in the database. The "magic" for mutating the data is once again the `EpisodeRemoteMediator` that we saw earlier. When the user scrolls, based on the `PagingConfig` of the `Pager` that can be found in the `EpisodesOverviewViewModel`, the library will call the mediator's `load()` method with a load type of `APPEND` and the logic described earlier will execute.

I've added some notes in the relevant code of the ViewModel so that the prefetching of pages can be adjusted in order to observe the various UI states.

# Pull To Refresh
Quite straightforward using the `PullToRefreshBox` composable and leveraging `LazyPagingItems<T>>`'s built-in `refresh()` method :)

# Background List Data Sync
For updating the episode list data, I went with the WorkManager library since it fullfills the usage requirements and simplifies the process of updating data in the background while the app is both in-use and in the background without requiring any user input. 

I defined a `CoroutineWorker` subclass named `UpdateEpisodeListWorker` which uses the pagination data stored in the database in order to determine which (if any) pages the user has already visited and thus stored locally. Then for each one of those pages, it re-fetches it and persists the data in the database so that the user gets an up-to-date snapshot! This approach in my opinion is efficient in the sense that it only fetches pages that have already been fetched by the user already. On a first run for example, the `doWork()` will simply conclude without doing any work since the user's first visit will load that first page automatically and thus subsequent `doWork()` calls will then perform updates. This way, no duplicate initial requests are made to the API.

As for the worker details, we can check out the `scheduleEpisodeDataSync()` in the `MainApplication` class. This method will be called when the app is opened and it sets up the `Constraints` and `WorkRequest` required for the enqueuing of the worker. Specifically, I've went with what I consider pragmatic and real-world constraints, namely to save the user's battery when it's low and bandwidth when on a metered connection, by making the assumption that keeping a list of episode previews up to date is less important than losing out on a low battery or incurring extra costs to the user.

Furthermore, the request is built with an Exponential back-off policy of 1 minute to reduce the effect of i.e. the network connection being down and the worker "spamming" requests via its retries with a Linear policy. The request is defined as Periodic so that it runs automatically on a schedule of a fixed 30 minute interval.

Finally, I leverage `enqueueUniquePeriodicWork()` in order to avoid re-triggering or duplicating work requests if work is already in progress and unfinished which would also be good for cases such as sending background analytics events where duplicate requests could cause issues.

# Character Export
This is a straightforward implementation, where the user can initiate an export request via a button in the character details page. The user is then prompted to select a destination for the file to be saved at, and the use-case leverages the exporter implementation in order to convert the character into a string (see overriden `toString()` method) and subsequently the string into a `ByteArray`, and finally write the bytes to the file.

# Local Room Database
TODO:SP
# Unit Tests
TODO:SP
# Instrumentation Tests
TODO:SP
