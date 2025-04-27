package msu.ru.webprac;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class WebTest {
    @Test
    void baseTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        assertEquals("Домашняя страница", driver.getTitle());
        driver.quit();
    }

    @Test
    void studentsInCourseTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/courses");
        assertEquals("Курсы", driver.getTitle());
        List<WebElement> cources = driver.findElements(By.id("course_name"));
        assertTrue(cources.size() > 2);
        cources.get(0).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals("Информация о курсе", driver.getTitle());

        List<WebElement> students = driver.findElements(By.id("student_name"));
        assertTrue(students.size() > 10);

        List<WebElement> professors = driver.findElements(By.id("professor_name"));
        assertTrue(professors.size() >= 1);

        driver.quit();
    }

    void setTI(ChromeDriver driver, String from, String to) {
        WebElement ti_from = driver.findElement(By.id("ti_from"));
        WebElement ti_to = driver.findElement(By.id("ti_to"));
        ti_from.clear();
        ti_from.sendKeys(from);
        ti_from.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        ti_to.clear();
        ti_to.sendKeys(to);
        ti_to.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement ti_submitButton = driver.findElement(By.id("ti_submitButton"));
        ti_submitButton.click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    void studentsTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/students");
        assertEquals("Студенты", driver.getTitle());
        List<WebElement> students = driver.findElements(By.id("student_name"));
        assertTrue(students.size() > 10);
        students.get(0).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals("Информация о студенте", driver.getTitle());

        List<WebElement> courses = driver.findElements(By.id("course_name"));
        assertTrue(courses.size() >= 1);

        setTI(driver, "01-01-2025", "01-01-2026");

        List<WebElement> lectures = driver.findElements(By.id("lecture_date"));
        assertTrue(lectures.size() >= 2);

        setTI(driver, "01-01-2035", "01-01-2036");

        lectures = driver.findElements(By.id("lecture_date"));
        assertTrue(lectures.isEmpty());

        WebElement no_lectures = driver.findElement(By.id("no_lectures"));
        assertEquals("На данный период нет ни одного занятия", no_lectures.getText());

        driver.quit();
    }

    @Test
    void professorsTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/professors");
        assertEquals("Лекторы", driver.getTitle());
        List<WebElement> professors = driver.findElements(By.id("professor_name"));
        assertTrue(professors.size() >= 5);
        professors.get(0).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals("Информация о лекторе", driver.getTitle());

        setTI(driver, "01-01-2025", "01-01-2026");

        List<WebElement> lectures = driver.findElements(By.id("lecture_date"));
        assertTrue(lectures.size() >= 2);

        setTI(driver, "01-01-2035", "01-01-2036");

        lectures = driver.findElements(By.id("lecture_date"));
        assertTrue(lectures.isEmpty());

        WebElement no_lectures = driver.findElement(By.id("no_lectures"));
        assertEquals("На данный период нет ни одного занятия", no_lectures.getText());

        driver.quit();
    }

    @Test
    void unexistingPage() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/thispagedoesnotexist");
        assertEquals("Ошибка", driver.getTitle());
        WebElement text = driver.findElement(By.id("error_text"));
        assertEquals("Неопознанная ошибка", text.getText());
        WebElement back = driver.findElement(By.id("error_back"));
        back.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        assertEquals("Домашняя страница", driver.getTitle());

        driver.quit();
    }

    @Test
    void wrongCourse() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/course?id=1000");
        assertEquals("Ошибка", driver.getTitle());
        WebElement text = driver.findElement(By.id("error_text"));
        assertEquals("Нет студента с id=1000", text.getText());
        WebElement back = driver.findElement(By.id("error_back"));
        back.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        assertEquals("Домашняя страница", driver.getTitle());

        driver.quit();
    }

    @Test
    void addLecture() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/courses");
        assertEquals("Курсы", driver.getTitle());
        List<WebElement> cources = driver.findElements(By.id("course_name"));
        assertTrue(cources.size() > 2);
        cources.get(0).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        WebElement button = driver.findElement(By.id("edit_course"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        button = new WebDriverWait(driver, Duration.ofSeconds(100)).until(
                ExpectedConditions.elementToBeClickable(By.id("add_lecture"))
        );
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        WebElement professor = new WebDriverWait(driver, Duration.ofSeconds(100)).until(
                ExpectedConditions.elementToBeClickable(By.id("professor"))
        );
        Select select = new Select(professor);
        select.selectByIndex(2);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement lecture_date = driver.findElement(By.id("lecture_date"));
        lecture_date.clear();
        lecture_date.sendKeys("05-05-2035");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement lecture_time = driver.findElement(By.id("lecture_time"));
        lecture_time.clear();
        lecture_time.sendKeys("10:10");
        lecture_time.click();
        lecture_time.sendKeys("AM");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement lecture_duration = driver.findElement(By.id("lecture_duration"));
        lecture_duration.clear();
        lecture_duration.sendKeys("90");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

        button = driver.findElement(By.id("submitButton"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.get("http://localhost:8080/schedule");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

        setTI(driver, "01-01-2035", "01-01-2036");
        List<WebElement> lectures = driver.findElements(By.id("remove_lecture"));
        assertEquals(1, lectures.size());
        lectures.get(0).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

        setTI(driver, "01-01-2035", "01-01-2036");
        lectures = driver.findElements(By.id("remove_lecture"));
        assertTrue(lectures.isEmpty());

        driver.quit();
    }

    @Test
    void addLecture2() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/schedule");

        WebElement button = driver.findElement(By.id("add_lecture"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        WebElement professor = new WebDriverWait(driver, Duration.ofSeconds(100)).until(
                ExpectedConditions.elementToBeClickable(By.id("professor"))
        );
        Select select_professor = new Select(professor);
        select_professor.selectByIndex(2);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement course = driver.findElement(By.id("course"));
        Select select_course = new Select(course);
        select_course.selectByIndex(2);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement lecture_date = driver.findElement(By.id("lecture_date"));
        lecture_date.clear();
        lecture_date.sendKeys("05-05-2035");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement lecture_time = driver.findElement(By.id("lecture_time"));
        lecture_time.clear();
        lecture_time.sendKeys("10:10");
        lecture_time.click();
        lecture_time.sendKeys("AM");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        WebElement lecture_duration = driver.findElement(By.id("lecture_duration"));
        lecture_duration.clear();
        lecture_duration.sendKeys("90");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

        button = driver.findElement(By.id("submitButton"));
        button.click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

        setTI(driver, "01-01-2035", "01-01-2036");
        List<WebElement> lectures = driver.findElements(By.id("remove_lecture"));
        assertEquals(1, lectures.size());
        lectures.get(0).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

        setTI(driver, "01-01-2035", "01-01-2036");
        lectures = driver.findElements(By.id("remove_lecture"));
        assertTrue(lectures.isEmpty());

        driver.quit();
    }

    @Test
    void testNewCourseAndStudent() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/courses");

        WebElement button = driver.findElement(By.id("add_course"));
        button.click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

        WebElement course_name = new WebDriverWait(driver, Duration.ofSeconds(100)).until(
                ExpectedConditions.elementToBeClickable(By.id("course_name"))
        ); // Не знаю почему Selenium не хочет по нормальному видеть именно этот элемент
        course_name.clear();
        course_name.sendKeys("test123test42");
        WebElement course_description = driver.findElement(By.id("course_description"));
        course_description.clear();
        course_description.sendKeys("Тестовое описание 1");
        WebElement course_duration = driver.findElement(By.id("course_duration"));
        course_duration.clear();
        course_duration.sendKeys("18");

        button = driver.findElement(By.id("submitButton"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        List<WebElement> lectures = driver.findElements(By.id("course"));
        button = null;
        for (WebElement elem : lectures) {
            if (elem.findElement(By.id("course_name")).getText().equals("test123test42")) {
                button = elem.findElement(By.id("course_name"));
                break;
            }
        }
        assertNotNull(button);
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        course_description = driver.findElement(By.id("course_description"));
        assertEquals("Тестовое описание 1", course_description.getText());

        List<WebElement> students = driver.findElements(By.id("student_name"));
        assertTrue(students.isEmpty());

        button = driver.findElement(By.id("edit_course"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        WebElement student = driver.findElement(By.id("student_id"));
        Select select = new Select(student);
        select.selectByIndex(20);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

        button = driver.findElement(By.id("submitButton10"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        course_description = driver.findElement(By.id("course_description"));
        course_description.clear();
        course_description.sendKeys("Тестовое описание 2");

        button = driver.findElement(By.id("submitButton"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);





        driver.get("http://localhost:8080/students");
        button = driver.findElement(By.id("add_student"));
        button.click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

        WebElement student_name = new WebDriverWait(driver, Duration.ofSeconds(100)).until(
                ExpectedConditions.elementToBeClickable(By.id("student_name"))
        ); // Не знаю почему Selenium не хочет по нормальному видеть именно этот элемент
        student_name.clear();
        student_name.sendKeys("FNU");
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        WebElement student_surname = driver.findElement(By.id("student_surname"));
        student_surname.clear();
        student_surname.sendKeys("LNU");
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        WebElement student_patronymic = driver.findElement(By.id("student_patronymic"));
        student_patronymic.clear();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        WebElement student_date = driver.findElement(By.id("student_date"));
        student_date.clear();
        student_date.sendKeys("04-04-2004");
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        button = driver.findElement(By.id("submitButton"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        students = driver.findElements(By.id("student"));
        button = null;
        for (WebElement elem : students) {
            if (elem.findElement(By.id("student_name")).getText().equals("FNU")) {
                button = elem.findElement(By.id("student_name"));
                break;
            }
        }
        assertNotNull(button);
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        student_patronymic = driver.findElement(By.id("student_patronymic"));
        assertEquals("", student_patronymic.getText());

        button = driver.findElement(By.id("edit_student"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        student_patronymic = driver.findElement(By.id("student_patronymic"));
        student_patronymic.clear();
        student_patronymic.sendKeys("PNU");
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        button = driver.findElement(By.id("submitButton"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        students = driver.findElements(By.id("student"));
        button = null;
        for (WebElement elem : students) {
            if (elem.findElement(By.id("student_name")).getText().equals("FNU")) {
                button = elem.findElement(By.id("student_name"));
                break;
            }
        }
        assertNotNull(button);
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        student_patronymic = driver.findElement(By.id("student_patronymic"));
        assertEquals("PNU", student_patronymic.getText());

        WebElement course = driver.findElement(By.id("course_id"));
        select = new Select(course);
        select.selectByVisibleText("test123test42");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

        button = driver.findElement(By.id("submitButton"));
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);




        driver.get("http://localhost:8080/courses");
        lectures = driver.findElements(By.id("course"));
        button = null;
        for (WebElement elem : lectures) {
            if (elem.findElement(By.id("course_name")).getText().equals("test123test42")) {
                button = elem.findElement(By.id("course_name"));
                break;
            }
        }
        assertNotNull(button);
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        course_description = driver.findElement(By.id("course_description"));
        assertEquals("Тестовое описание 2", course_description.getText());

        students = driver.findElements(By.id("student_name"));
        assertEquals(2, students.size());

        button = driver.findElement(By.id("remove_course"));
        button.click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

        lectures = driver.findElements(By.id("course"));
        button = null;
        for (WebElement elem : lectures) {
            if (elem.findElement(By.id("course_name")).getText().equals("test123test42")) {
                button = elem.findElement(By.id("course_name"));
                break;
            }
        }
        assertNull(button);




        driver.get("http://localhost:8080/students");
        students = driver.findElements(By.id("student"));
        button = null;
        for (WebElement elem : students) {
            if (elem.findElement(By.id("student_name")).getText().equals("FNU")) {
                button = elem.findElement(By.id("student_name"));
                break;
            }
        }
        assertNotNull(button);
        button.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        button = driver.findElement(By.id("remove_student"));
        button.click();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

        students = driver.findElements(By.id("student"));
        button = null;
        for (WebElement elem : students) {
            if (elem.findElement(By.id("student_name")).getText().equals("FNU")) {
                button = elem.findElement(By.id("student_name"));
                break;
            }
        }
        assertNull(button);


        driver.quit();
    }
}

