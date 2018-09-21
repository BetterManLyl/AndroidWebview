/**
 * 'Lecture'类的构造函数
 * @param name 用户名
 * @param teacher 教师
 * @constructor
 */
var name;
var teacher;

function Lecture(name, teacher) {
    this.name = name;
    this.teacher = teacher;
}

Lecture.prototype.display = function () {
    return this.teacher + "is teaching" + this.name;
}

function Schedule(lectures) {
    this.lectures = lectures;
}

Schedule.prototype.display = function () {
    var str = "";
    for (var i = 0; i < this.lectures.length; i++) {
        str += this.lectures[i].display() + " "
    }
    return str;

}

var mySchedule = new Schedule([new Lecture("lyl", "lylt"), new Lecture("cml", "cmlt"), new Lecture("ly", "lyt")])

alert(mySchedule.display())

/**
 *
 */
function add() {

}

