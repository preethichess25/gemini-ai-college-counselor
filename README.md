TRS Gemini AI College Counselor
Developed By
* Vinupriya Rajkumar
* Preethi Rajkumar
* Karthikeyini Rajkumar

1. Problem Statement (WHY)

Every year, thousands of rural students, first-generation graduates, and underprivileged learners struggle to choose the right college due to systemic barriers:
* Lack of Personalized Counseling: Students do not have access to unbiased, knowledgeable mentors.
* Information Asymmetry: They are unaware of real fees, campus infrastructure, hostel availability and actual placement statistics.
* Predatory Practices: Many are misled by commission-based agents or confusing online lists containing inaccurate or incomplete data.
* These challenges often result in wrong college choices, financial losses, emotional stress and limited career opportunities — especially for students who deserve better support.

Personal Motivation

The inspiration for this project comes from a personal family experience.
My elder sister, a first-generation graduate, struggled to identify the right college online. A naming similarity led her to mistakenly enroll in the wrong institution, leading to emotional distress, financial loss and wasted time. By the time she corrected the mistake, most seats in reputable colleges were already filled.

This incident highlighted how widespread and harmful these information gaps are — and inspired us to build a system that ensures no student ever repeats the same experience.

2. Mission and Solution (WHAT)

The mission of the TRS Gemini AI College Counselor is simple:

To help every student, especially those from rural and economically weaker communities, get transparent, accurate, personalized college guidance totally free of cost.

This solution uses Google’s advanced Gemini AI and custom tools to generate data-driven, fair, and personalized college recommendations based on each student’s unique profile.

3. End-to-End Workflow (What This Project Does)

The system provides a complete AI-driven counseling pipeline:
* Collects Student Profile - Course preference, marks, budget, city, and constraints.
* Searches Colleges - Using a dataset of sample colleges across major cities (colleges.json).
* Applies Filters - Removes colleges that fail mandatory requirements (cutoff, fees, location, hostel, etc.).
* Ranks Colleges - Using a Fit Score computed from:
                                     Budget suitability
                                     Placement performance
                                     Infrastructure rating
                                     Course availability
                                     Distance/location
                                     Hostel/transport availability
* Generates Final Recommendation Report - A clear, student-friendly summary containing:
                                     Top recommended colleges
                                     Why each college is a good fit
                                     Fee details
                                     Facilities
                                     Placement summary
                                     Next steps
* Web UI with Google ADK Dev Console - Interaction is handled via a dedicated chat interface, allowing visual communication with agents through the Dev UI.

4. Technical Architecture (HOW)

This project uses a Multi-Agent System built with the Google Agent Development Kit (ADK) using Gemini 2.5 models. The architecture is a precise Agent-to-Agent (A2A) pipeline ensuring complex tasks are handled efficiently by specialized, autonomous agents

Agents Used
* Root Orchestrator Agent - Controls the workflow, maintains session state, and routes tasks.
* ProfileCollectorAgent - Collects and validates student information.
* ResearchAgent - Searches dataset + Google Search results (if enabled) to gather detailed college information
* FilterAgent - Removes colleges that don’t meet mandatory criteria.
* RankingAgent - Computes Fit Score using the custom FitScoreTool.
* SummaryAgent - Generates the final recommendation summary/document using Gemini.
* Tools Used
             CollegeDatasetTool – Loads and queries college data
             FitScoreTool – Computes match score
             MCPFileTool – Creates final report (PDF / text output)
             Google Search Tool – Optional real-time info
             Code Executor Tool (ADK built-in) – Filtering & scoring
* ADK Concepts Demonstrated
             ✔ Multi-agent system
             ✔ Sequential agents
             ✔ Custom tools
             ✔ Session management (InMemorySessionService)
             ✔ Context engineering
             ✔ Logging & debugging
             ✔ A2A communication
             ✔ Gemini calling tools
             ✔ State flow between agents

5. Project Structure
The project follows a standard Java ADK structure:
gemini-ai-college-counselor	
├──src/main/java/com/vpk/ai/
│	├── agents/
│	│    ├── ProfileCollectorAgent.java
│	│    ├── ResearchAgent.java
│	│    ├── FilterAgent.java
│	│    ├── RankingAgent.java
│	│    ├── SummaryAgent.java
│	│    └── AgentsFactory.java
│	│
│	├── models/
│	│    ├── UserProfile.java
│	│    └── CollegeRecord.java
│	│
│	├── data/
│	│    └── CollegeDataLoader.java
│	│
│	├── tools/
│	│    ├── CollegeDatasetTool.java
│	│    ├── CollegeSearchInput.java
│	│    ├── FitScoreTool.java
│	│    └── MCPFileTool.java
│	│
│	└── AgentsFactory.java
│	└── CollegeCounselorApp.java
└──src/main/resources/
   ├── colleges.json
	 └── config.properties

6. Tech Stack:
Language: Java 21
LLM: Google Gemini 2.5 Flash
Framework: Google ADK 0.4
Tools: JSON dataset, FitScoreTool, MCPFileTool
Web UI: ADK Web Server

7. How It Works (Deployment)
Clone the repository.
To run the application, you need a Gemini API Key and Maven.
Generate Api Key from the Google AI Studio (https://aistudio.google.com/app/apikey?_gl=1*mvky98*_ga*MzgxMjU3Nzk4LjE3NjQ1MzE4NTI.*_ga_P1DBVKWT6V*czE3NjQ3MTU4MjckbzMkZzEkdDE3NjQ3MTU5NTYkajckbDAkaDE5NjAxODkzNzU.)
Add gemini.api.key under /gemini-ai-college-counselor/src/main/resources/config.properties
Run the App:
* mvn clean install
* mvn exec:java
* Then open the Google ADK Dev UI in your browser: 👉 http://localhost:8000 and type "HI"
* Next give your % of cutoff, location you prefer and course you are looking for.  (Example 93%, madurai colleges, computer science Engineering)
* It will give you top 3 colleges with detailed details.

* Sample Output
 User : Hi
 AI : Hi! I can help you find colleges based on cutoff, course, location, budget, or rank. Tell me any detail and I will help you.
User : 93%, madurai colleges, computer science
AI : Here is your personalized college recommendation report based on your academic profile and interests:

1) Top 3 Recommendations with Reasons
All evaluated colleges received an excellent fit score of 93, indicating they are all strong matches for your interest in Computer Science in Madurai. The primary differentiators among them are placement percentages and tuition costs.

Madurai Institute of Technology (CS)
Reasoning: This college stands out with the highest placement percentage at 85%, indicating strong career prospects post-graduation. Its tuition fee of ₹1.2L is also very competitive, making it an excellent balance of quality and cost.
SRI College of Engineering (CS)
Reasoning: SRI College offers the second-highest placement rate at 70%, which is still a very good outcome. The tuition fee is ₹1.4L, placing it in the mid-range. It's a solid choice that balances good placement with a moderate cost.
K.L.N. College of Engineering (CS)
Reasoning: While K.L.N. College has the lowest placement percentage at 55% among the options, it boasts the most affordable tuition fee at ₹1.1L. This makes it an important consideration if budget is a primary concern, offering a clear trade-off between cost and placement rate.

8 .Project Flow:
User opens the app or web UI.
Root orchestrator agent initializes sub-agents.
ProfileCollectorAgent greets the user and collects academic and personal info.
ResearchAgent searches dataset for colleges matching criteria.
FilterAgent applies rules to narrow the list.
RankingAgent scores colleges using FitScoreTool.
SummaryAgent compiles final report with top 3 recommendations, cost comparisons, College website link and scholarship info.
The system displays output in UI using MCPFileTool.

9. Team Member	Core Contribution Area	Key Achievements
Vinupriya Rajkumar	- System Architecture & Core Development - Proposed the core idea, Implemented the final Multi-Agent orchestration logic, refined the custom RankingAgent and FitScoreTool, performed critical UI testing, and ensured compliance with ADK standards.
Preethi Rajkumar	- Initial Research & Agent Prototyping	 - Proposed the core problem statement, researched the feasibility of using the Java ADK for agent development and prototyped the initial ProfileCollectorAgent and ResearchAgent based on sample guides.
Karthiyini Rajkumar	- Data Modeling & Tool Definition	     - Proposed the core idea, Led the research into the college data structure, defined the constraints and scoring logic and assisted in creating the initial logic for the FilterAgent and SummaryAgent.

10. System Architecture <img width="1024" height="1536" alt="image" src="https://github.com/user-attachments/assets/32ce06fc-1dca-4990-be6b-5ec5b3373dbd" />

