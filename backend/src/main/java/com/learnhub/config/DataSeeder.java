package com.learnhub.config;

import com.learnhub.entity.*;
import com.learnhub.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final CourseRepository courses;
    private final CourseModuleRepository modules;
    private final LessonRepository lessons;
    private final QuizRepository quizzes;
    private final QuestionRepository questions;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository users, CourseRepository courses, CourseModuleRepository modules,
                      LessonRepository lessons, QuizRepository quizzes, QuestionRepository questions,
                      PasswordEncoder encoder) {
        this.users = users;
        this.courses = courses;
        this.modules = modules;
        this.lessons = lessons;
        this.quizzes = quizzes;
        this.questions = questions;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;

        users.save(User.builder().name("Priya Admin").email("admin@learnhub.com")
                .password(encoder.encode("Admin@123")).role(Role.ADMIN).bio("Platform guardian").avatarColor("#1B4332").build());
        User inst = users.save(User.builder().name("Maya Green").email("instructor@learnhub.com")
                .password(encoder.encode("Instructor@123")).role(Role.INSTRUCTOR)
                .bio("Educator who believes curiosity grows like a forest.").avatarColor("#40916C").build());
        users.save(User.builder().name("Aarav Student").email("student@learnhub.com")
                .password(encoder.encode("Student@123")).role(Role.STUDENT)
                .bio("Ready to grow, one lesson at a time.").avatarColor("#74C69D").build());

        seedCourse(inst, "Java Programming",
                "Learn Java from the fundamentals through object-oriented programming, collections, exceptions, and building REST APIs.",
                "Programming", "Beginner", "java", 10,
                new String[][]{
                        {"Java Foundations", "Java programs are organized into classes and methods. The JVM runs compiled bytecode across supported platforms.",
                                "Variables store values, and primitive types include int, double, char, and boolean. Use clear names and choose the type that fits the data."},
                        {"Object-Oriented Java", "Classes define state and behavior. Objects are instances of classes, and constructors initialize new objects.",
                                "Encapsulation protects internal state. Inheritance and interfaces help organize related behavior, while exceptions make failures easier to handle."}
                },
                q("What executes Java bytecode?", "JVM", "HTML", "SQL engine", "CSS", "A"),
                q("Which keyword creates an object?", "class", "new", "import", "extends", "B"));

        seedCourse(inst, "Python Programming",
                "Start with Python syntax, variables, conditions, loops, functions, collections, and practical scripts.",
                "Programming", "Beginner", "python", 8,
                new String[][]{
                        {"Python Basics", "Python uses indentation to group statements. Variables are created by assignment, and Python infers their types at runtime.",
                                "Use if and elif for decisions and for or while loops to repeat work. Lists keep ordered items and dictionaries store key-value pairs."},
                        {"Functions and Files", "Functions defined with def package reusable steps. Parameters accept input and return sends a result back to the caller.",
                                "Use with open(...) to work with files safely. Handle expected errors with try and except rather than hiding every exception."}
                },
                q("Which keyword defines a function in Python?", "func", "function", "def", "method", "C"),
                q("Which collection stores key-value pairs?", "List", "Dictionary", "Tuple", "Set", "B"));

        seedCourse(inst, "C Programming",
                "Understand procedural programming with variables, control flow, functions, arrays, pointers, structures, and memory basics.",
                "Programming", "Beginner", "c", 8,
                new String[][]{
                        {"C Fundamentals", "A C program commonly begins execution in main. Include standard headers for library functions such as printf.",
                                "Use if, switch, loops, and functions to organize logic. Arrays store multiple values of the same type in contiguous elements."},
                        {"Pointers and Structures", "A pointer stores a memory address. Dereference a valid pointer with * and take an object's address with &.",
                                "Structures group fields of different types. Check array bounds and manage dynamically allocated memory carefully."}
                },
                q("Which function is the usual entry point of a C program?", "start()", "main()", "run()", "printf()", "B"),
                q("What does a pointer store?", "Only a character", "A memory address", "A loop", "A file name", "B"));

        seedCourse(inst, "C++ Programming",
                "Learn modern C++ basics, classes, references, STL containers, algorithms, and safer resource management.",
                "Programming", "Beginner", "cpp", 9,
                new String[][]{
                        {"C++ Essentials", "C++ supports procedural and object-oriented styles. Use std::cout for output and include the headers your program needs.",
                                "References provide aliases to existing objects. Use const when a value should not be modified through that reference."},
                        {"Classes and STL", "Classes combine data and methods. Constructors establish a valid initial state for objects.",
                                "The Standard Template Library includes containers such as vector and map, plus algorithms such as sort and find."}
                },
                q("Which container is a resizable sequence in C++?", "std::vector", "std::fixed", "std::bytecode", "std::pointer", "A"),
                q("Which keyword declares a class?", "object", "structonly", "class", "define", "C"));

        seedCourse(inst, "JavaScript Programming",
                "Build interactive web experiences using JavaScript variables, functions, arrays, objects, DOM events, and async code.",
                "Web Development", "Beginner", "javascript", 8,
                new String[][]{
                        {"JavaScript Foundations", "Use const for bindings that should not be reassigned and let for values that need reassignment.",
                                "Functions can be declared or expressed as arrow functions. Arrays and objects are common ways to organize data."},
                        {"Browser and Async JavaScript", "The DOM represents a web page as nodes that JavaScript can query and update.",
                                "Event listeners respond to user actions. Promises and async/await help work with asynchronous tasks such as API requests."}
                },
                q("Which keyword declares a block-scoped variable that can be reassigned?", "let", "fixed", "dim", "varname", "A"),
                q("What is commonly used to handle a button click?", "A database table", "An event listener", "A CSS selector only", "A package lock", "B"));

        seedCourse(inst, "HTML & CSS Web Development",
                "Create accessible, responsive web pages with semantic HTML, CSS selectors, layouts, flexbox, grid, and media queries.",
                "Web Development", "Beginner", "htmlcss", 7,
                new String[][]{
                        {"HTML Page Structure", "HTML elements describe content structure. Use headings in a logical order and semantic elements such as header, nav, main, and footer.",
                                "Links navigate to resources, forms collect input, and useful alt text makes informative images accessible."},
                        {"CSS and Responsive Layout", "CSS rules combine selectors and declarations to style page elements. The box model includes content, padding, border, and margin.",
                                "Flexbox aligns items along an axis, Grid manages rows and columns, and media queries adapt layouts to smaller screens."}
                },
                q("Which HTML element creates a hyperlink?", "<linkto>", "<a>", "<href>", "<url>", "B"),
                q("Which CSS feature adapts styles to screen conditions?", "Media queries", "SQL joins", "Java classes", "HTML comments", "A"));

        seedCourse(inst, "SQL & MySQL",
                "Learn relational database basics, SELECT queries, filtering, joins, grouping, subqueries, and table design with MySQL.",
                "Database", "Beginner", "mysql", 7,
                new String[][]{
                        {"Querying Data", "SELECT chooses columns, FROM names a table, and WHERE filters rows that match a condition.",
                                "ORDER BY sorts results. GROUP BY combines rows into groups, and aggregate functions such as COUNT and AVG summarize values."},
                        {"Joins and Database Design", "INNER JOIN returns matching rows from related tables. LEFT JOIN also preserves rows from the left table when no match exists.",
                                "Primary keys identify rows and foreign keys represent relationships. Use parameterized queries in applications to avoid SQL injection."}
                },
                q("Which clause filters rows before grouping?", "ORDER BY", "WHERE", "SELECT", "LIMIT", "B"),
                q("Which key uniquely identifies a row?", "Foreign key only", "Duplicate key", "Primary key", "Sort key", "C"));

        seedCourse(inst, "Data Structures & Algorithms",
                "Practice problem-solving with arrays, linked lists, stacks, queues, trees, searching, sorting, and algorithm complexity.",
                "Computer Science", "Intermediate", "dsa", 12,
                new String[][]{
                        {"Core Data Structures", "Arrays provide indexed access. Linked lists connect nodes, while stacks use last-in-first-out order and queues use first-in-first-out order.",
                                "Trees model hierarchical data. Choose a structure based on the operations your program performs most often."},
                        {"Algorithms and Complexity", "Binary search works on sorted data and repeatedly halves the search range.",
                                "Merge sort and quicksort are divide-and-conquer sorting algorithms. Big-O notation describes how time or memory use grows with input size."}
                },
                q("What must be true to use binary search correctly?", "The data is sorted", "The data has no numbers", "The list has one item only", "Every value is unique", "A"),
                q("Which notation describes growth as input size increases?", "RGB", "Big-O", "HTML", "JVM", "B"));


        seedCourse(inst, "Database Management Systems (DBMS)",
                "Learn ER modeling, relational algebra, SQL, normalization, transactions, concurrency control, and recovery.",
                "Database", "Beginner", "mysql", 12,
                new String[][]{
                        {"Database Foundations", "A DBMS stores and manages related data. ER diagrams model entities, attributes, relationships, and cardinality.", "Relational tables use candidate keys, primary keys, and foreign keys to represent data and relationships."},
                        {"Normalization and Transactions", "Normalization reduces update anomalies using functional dependencies and forms such as 1NF, 2NF, 3NF, and BCNF.", "ACID transactions provide atomicity, consistency, isolation, and durability. Indexes, locks, and recovery logs support reliable access."}
                }, q("Which key uniquely identifies each row?", "Primary key", "Sort key", "Duplicate key", "Display key", "A"), q("What does ACID describe?", "CSS rules", "Transaction properties", "Network layers", "Java types", "B"));

        seedCourse(inst, "Object-Oriented Programming", "Master classes, objects, encapsulation, inheritance, polymorphism, abstraction, interfaces, and design principles.",
                "Programming", "Beginner", "java", 8,
                new String[][]{{"OOP Foundations", "Classes define fields and methods while objects are instances with state and behavior.", "Encapsulation controls access to state. Abstraction exposes essential behavior while hiding implementation details."}, {"Reuse and Polymorphism", "Inheritance models an is-a relationship, while composition builds objects from collaborators.", "Polymorphism allows a common interface to support multiple implementations. Prefer clear responsibilities and loose coupling."}},
                q("Which concept hides internal state behind a public interface?", "Encapsulation", "Compilation", "Iteration", "Indexing", "A"), q("Which concept allows multiple implementations behind one interface?", "Polymorphism", "Parsing", "Serialization only", "Looping", "A"));

        seedCourse(inst, "Operating Systems", "Study processes, threads, scheduling, synchronization, deadlocks, memory management, file systems, and security.",
                "Computer Science", "Intermediate", "dsa", 12,
                new String[][]{{"Processes and Scheduling", "A process is a running program with its own resources. Threads share process resources and execute independently.", "Schedulers allocate CPU time using algorithms such as FCFS, SJF, priority scheduling, and round robin."}, {"Memory and Concurrency", "Virtual memory maps process address spaces to physical memory using pages and page tables.", "Mutexes and semaphores coordinate concurrent work. Deadlock analysis considers mutual exclusion, hold-and-wait, no preemption, and circular wait."}},
                q("Which is a CPU scheduling algorithm?", "Round robin", "HTML grid", "SQL join", "DNS lookup", "A"), q("What maps virtual pages to physical frames?", "Page table", "Stack trace", "CSS selector", "Socket URL", "A"));

        seedCourse(inst, "Computer Networks", "Learn network models, Ethernet, IP addressing, subnetting, routing, TCP, UDP, DNS, HTTP, and network security.",
                "Computer Science", "Intermediate", "dsa", 10,
                new String[][]{{"Network Fundamentals", "The OSI model separates communication into seven conceptual layers. TCP/IP groups practical internet protocols into a smaller stack.", "IPv4 addresses identify interfaces. Subnet masks divide networks into prefixes and host portions."}, {"Transport and Application", "TCP provides ordered reliable byte streams, while UDP provides datagrams without built-in delivery guarantees.", "DNS resolves names to records. HTTP transfers web resources, and TLS helps protect data in transit."}},
                q("Which protocol provides a reliable ordered byte stream?", "TCP", "UDP", "DNS", "ARP only", "A"), q("What does DNS primarily resolve?", "Names to records such as IP addresses", "Java to bytecode", "SQL to tables", "Images to CSS", "A"));

        seedCourse(inst, "Computer Organization and Architecture", "Explore binary arithmetic, digital logic, CPU datapaths, instruction sets, pipelining, memory hierarchy, and I/O.",
                "Computer Science", "Intermediate", "dsa", 10,
                new String[][]{{"Digital and CPU Basics", "Digital systems encode values using binary digits. Logic gates combine inputs to produce outputs.", "A CPU uses registers, an arithmetic logic unit, a control unit, and a datapath to execute instructions."}, {"Memory and Performance", "Caches keep frequently used data close to the processor. Locality helps explain why caches improve average access time.", "Pipelining overlaps instruction stages. Hazards can require forwarding, stalls, or branch prediction."}},
                q("What does ALU stand for?", "Arithmetic Logic Unit", "Advanced Link Utility", "Array Lookup Unit", "Application Layer User", "A"), q("What is a cache designed to improve?", "Average access time", "Source formatting", "Password length", "Screen brightness", "A"));

        seedCourse(inst, "Software Engineering", "Learn requirements, design, architecture, version control, testing, agile workflows, deployment, and maintenance.",
                "Software Development", "Beginner", "javascript", 9,
                new String[][]{{"Requirements and Design", "Requirements describe what a system must do and the constraints under which it operates.", "Design separates responsibilities into cohesive components and defines clear interfaces."}, {"Testing and Delivery", "Unit tests check small units, integration tests check collaboration, and end-to-end tests validate user workflows.", "Version control records changes. Continuous integration automates checks so defects are found earlier."}},
                q("What does a unit test primarily verify?", "A small unit of behavior", "An entire company network", "A database password only", "A color palette", "A"), q("What does version control track?", "Changes to files over time", "Only screen size", "Only runtime memory", "Only video playback", "A"));

        seedCourse(inst, "Web Development", "Learn how the web works, build responsive accessible interfaces, consume APIs, and deploy websites.",
                "Web Development", "Beginner", "htmlcss", 10,
                new String[][]{{"Web Foundations", "Browsers request resources from servers using HTTP. HTML defines structure, CSS controls presentation, and JavaScript adds behavior.", "Semantic markup and accessible forms help people navigate and use web pages across devices."}, {"Responsive Sites and Deployment", "Responsive design adapts layouts to different viewports using flexible units, layout systems, and media queries.", "Use browser developer tools to inspect errors, optimize assets, and verify navigation before deployment."}},
                q("Which technology primarily structures web page content?", "HTML", "SQL", "JVM", "DNS", "A"), q("What is responsive design intended to support?", "Different screen sizes and devices", "Only one fixed width", "Only server logs", "Only database backups", "A"));

        seedCourse(inst, "Backend Development", "Understand REST APIs, HTTP, validation, authentication, databases, error handling, testing, and deployment.",
                "Software Development", "Intermediate", "javascript", 10,
                new String[][]{{"HTTP and APIs", "REST APIs expose resources through predictable URLs and HTTP methods such as GET, POST, PUT, PATCH, and DELETE.", "Validate incoming data, use meaningful status codes, and return consistent error responses."}, {"Persistence and Security", "Repositories separate data access from business logic. Transactions keep related database changes consistent.", "Hash passwords, authorize every protected action, validate inputs, and keep credentials out of source control."}},
                q("Which HTTP method is commonly used to retrieve a resource?", "GET", "POST", "PATCH", "DELETE", "A"), q("How should passwords be stored?", "As secure hashes", "As plain text", "In URL parameters", "Inside frontend source", "A"));

        seedCourse(inst, "React.js", "Build component-based interfaces using JSX, props, state, hooks, routing, forms, API requests, and testing.",
                "Web Development", "Intermediate", "javascript", 9,
                new String[][]{{"Components and State", "React components render UI from props and state. JSX provides a readable syntax for describing elements.", "Use state for changing local data and effects for synchronizing with external systems."}, {"Routing and Data", "Client-side routing maps URLs to views without requiring a full page reload.", "Handle loading and error states when fetching APIs, and keep components reusable and accessible."}},
                q("What triggers a React component to update when local data changes?", "State update", "Changing a CSS comment", "Renaming a folder only", "A database index", "A"), q("What is a React component?", "A reusable UI building block", "A SQL table", "A network cable", "A compiler flag", "A"));

        seedCourse(inst, "Spring Boot", "Build Java services with dependency injection, REST controllers, validation, JPA, security, configuration, and tests.",
                "Backend Development", "Intermediate", "java", 10,
                new String[][]{{"Spring Foundations", "Spring dependency injection manages object creation and relationships through the application context.", "REST controllers map HTTP requests to Java methods and return response data."}, {"Persistence and Security", "Spring Data JPA repositories simplify common persistence operations over entity models.", "Use validation, explicit authorization, externalized configuration, and automated tests for dependable services."}},
                q("What does dependency injection help manage?", "Object dependencies", "CSS gradients", "Image dimensions", "Browser bookmarks", "A"), q("Which technology maps Java entities to relational data?", "JPA", "DOM", "SVG", "DNS", "A"));

        seedCourse(inst, "Git and GitHub", "Practice repositories, commits, branches, merges, pull requests, conflict resolution, issues, and collaboration.",
                "Developer Tools", "Beginner", "javascript", 5,
                new String[][]{{"Git Basics", "A repository tracks project history. A commit records a snapshot of staged changes.", "Branches allow isolated work, while merges combine histories and may require conflict resolution."}, {"Collaborating on GitHub", "A remote connects a local repository to a shared hosting service.", "Pull requests support review before changes merge. Never commit secrets, credentials, or generated dependency folders."}},
                q("Which command records a local snapshot of staged changes?", "git commit", "git paint", "git browse", "git style", "A"), q("What is a pull request commonly used for?", "Reviewing proposed changes", "Changing monitor brightness", "Running a database server", "Formatting a video", "A"));

        seedCourse(inst, "Data Science Fundamentals", "Learn data cleaning, descriptive statistics, visualization, exploratory analysis, and introductory machine learning workflows.",
                "Data Science", "Beginner", "python", 9,
                new String[][]{{"Data Preparation", "Data analysis begins by understanding columns, types, missing values, duplicates, and data quality.", "Summaries and visualizations help reveal distributions, outliers, and relationships."}, {"Modeling and Evaluation", "Train and test splits help estimate how well a model generalizes to unseen examples.", "Choose metrics appropriate to the task and document assumptions, limitations, and potential bias."}},
                q("Why keep a test set separate from training data?", "To evaluate generalization", "To increase image size", "To avoid naming variables", "To replace all missing data automatically", "A"), q("What helps reveal distributions and outliers?", "Visualization", "A CSS reset", "A password hash", "A route path", "A"));

    }

    private record Q(String text, String a, String b, String c, String d, String correct) {}

    private Q q(String text, String a, String b, String c, String d, String correct) {
        return new Q(text, a, b, c, d, correct);
    }

    private void seedCourse(User instructor, String title, String desc, String category, String level, String thumb,
                            int hours, String[][] moduleLessons, Q... quizQs) {
        Course course = courses.save(Course.builder()
                .title(title).description(desc).category(category).level(level)
                .thumbnail(thumb).durationHours(hours).passScore(70.0).instructor(instructor).build());
        Lesson last = null;
        int mo = 1;
        for (String[] pair : moduleLessons) {
            CourseModule mod = modules.save(CourseModule.builder().title(pair[0]).course(course).moduleOrder(mo++).build());
            for (int i = 1; i < pair.length; i++) {
                last = lessons.save(Lesson.builder()
                        .title(pair[0] + " · Part " + i)
                        .content("<p>" + pair[i] + "</p><p>Take a slow breath. Write one sentence in your own words before you continue — that is how knowledge takes root.</p>")
                        .videoUrl("")
                        .durationMinutes(12 + i * 3)
                        .module(mod).lessonOrder(i).build());
            }
        }
        if (last != null && quizQs.length > 0) {
            Quiz quiz = quizzes.save(Quiz.builder().title(title + " Checkpoint").lesson(last).course(course).build());
            for (Q qn : quizQs) {
                questions.save(Question.builder().quiz(quiz).questionText(qn.text())
                        .optionA(qn.a()).optionB(qn.b()).optionC(qn.c()).optionD(qn.d())
                        .correctOption(qn.correct()).build());
            }
        }
    }
}
