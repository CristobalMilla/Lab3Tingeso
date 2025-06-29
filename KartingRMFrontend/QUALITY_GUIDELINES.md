# Frontend Quality Guidelines for KartingRM

This document outlines the essential libraries and configurations for achieving Nielsen's 10 Usability Heuristics compliance and a SUS score of 75+ in a learning environment with Spring Boot + PostgreSQL backend.

## Project Context
- **Purpose**: Educational project for user quality testing and learning
- **Backend**: Spring Boot with Maven
- **Database**: PostgreSQL
- **Environment**: Development/Testing only (not production)
- **Platform**: Windows development environment

## Final Dependencies & Quality Alignment

### **Production Dependencies (19 packages)**

#### UI/UX & Nielsen Heuristics Compliance
- **@mui/material, @emotion/react, @emotion/styled**: Material Design system for consistency (Heuristics #4, #8)
- **@mui/icons-material**: Standardized visual communication (Heuristic #2)
- **@mui/lab**: Advanced components for enhanced user experience
- **react-router-dom**: Consistent navigation and user control (Heuristics #3, #4)
- **react-hot-toast**: Immediate user feedback (Heuristic #1)
- **react-hook-form, @hookform/resolvers, yup**: Form validation with clear error prevention and recovery (Heuristics #5, #9)

#### Performance & Backend Integration
- **axios**: HTTP client for Spring Boot REST API communication
- **@tanstack/react-query + devtools**: Data fetching with loading states and caching (Heuristic #1)
- **react-helmet-async**: SEO and metadata management

#### Utility & Maintainability
- **clsx**: Conditional CSS class management
- **date-fns**: Lightweight date formatting for user-friendly displays
- **lodash**: Utility functions reducing code complexity

### **Development Dependencies (8+ packages)**

#### Testing (Selenium Preparation + >50% Automation)
- **@testing-library/react**: Component testing compatible with Selenium WebDriver
- **@testing-library/jest-dom**: DOM assertion utilities
- **@testing-library/user-event**: User interaction simulation for automated tests
- **vitest**: Fast testing framework with coverage reporting
- **jsdom**: DOM environment for headless testing

#### Code Quality (SonarQube <5% Technical Debt)
- **ESLint**: Code linting with accessibility and import rules
- **TypeScript**: Static typing for maintainability
- **@vitest/ui**: Visual testing interface for development

## Nielsen Heuristics Implementation Strategy

### 1. **Visibility of System Status**
- Loading indicators via React Query
- Toast notifications for all user actions
- Progress indicators for multi-step processes

### 2. **Match Between System and Real World**
- Material-UI icons following familiar conventions
- Real-world language in all interface text
- Logical information architecture

### 3. **User Control and Freedom**
- Clear navigation with React Router
- "Cancel" and "Back" options on all forms
- Undo functionality where applicable

### 4. **Consistency and Standards**
- Material-UI design system enforcement
- Consistent URL patterns and navigation
- Standardized form layouts and validation messages

### 5. **Error Prevention**
- Yup schema validation preventing invalid inputs
- Confirmation dialogs for destructive actions
- Input constraints and helper text

### 6. **Recognition Rather Than Recall**
- Breadcrumb navigation components
- Recently accessed items display
- Clear labels and visual hierarchy

### 7. **Flexibility and Efficiency of Use**
- Keyboard navigation support
- Advanced filtering and search options
- User preference persistence

### 8. **Aesthetic and Minimalist Design**
- Clean Material-UI interface
- Focused content presentation
- Proper whitespace and typography

### 9. **Help Users Recognize, Diagnose, and Recover from Errors**
- Clear, actionable error messages
- Helpful suggestions for error resolution
- Error boundary components for graceful degradation

### 10. **Help and Documentation**
- Contextual tooltips and help text
- Inline guidance for complex operations
- Accessible help sections

## Testing Strategy for Quality Compliance

### **Functionality Testing (>50% Automation)**
1. **Unit Tests**: React Testing Library for component behavior
2. **Integration Tests**: API communication with Spring Boot backend
3. **E2E Tests**: Selenium WebDriver for user workflows
4. **Accessibility Tests**: Automated a11y compliance checking
5. **Performance Tests**: Frontend metrics and JMeter backend testing

### **Usability Testing (SUS 75+ Target)**
1. **Task-based User Testing**: Real user scenarios
2. **SUS Questionnaire**: Standardized 10-question survey
3. **Nielsen Heuristic Evaluation**: Expert review checklist
4. **A/B Testing**: Interface variant comparison

### **Performance Testing (JMeter Integration)**
- Frontend performance metrics
- Backend API load testing with JMeter
- Database query optimization with PostgreSQL
- Network latency simulation

### **Maintainability (SonarQube <5%)**
- Code coverage reporting
- Technical debt monitoring
- Code complexity analysis
- Import organization and linting

## Development Workflow

### Quality Check Commands
```bash
npm run quality:check    # Full quality pipeline
npm run lint            # ESLint checking
npm run type-check      # TypeScript validation
npm run test:run        # Automated test suite
npm run test:coverage   # Coverage reporting
```

### Testing Commands
```bash
npm run test           # Interactive testing
npm run test:ui        # Visual test interface
npm run dev           # Development server
```

## Windows Development Considerations
- PowerShell commands for file operations
- Windows-specific path handling
- Local PostgreSQL setup for development
- Maven integration for Spring Boot backend testing

## Next Implementation Steps
1. Design the three core karting functionalities
2. Implement Nielsen heuristic-compliant UI components
3. Create comprehensive test suites (unit + integration + E2E)
4. Set up Selenium testing environment
5. Integrate SonarQube code quality monitoring
6. Conduct SUS testing sessions with real users
7. Performance testing with JMeter against Spring Boot APIs


