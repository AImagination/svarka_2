package org.example.svarka.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.navigation.RoutePrefix.Companion.value
import com.varabyte.kobweb.silk.components.forms.InputStyle
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalComposeWebApi::class)
@Page
@Composable
fun HomePage() {

    var isDirectionTheSame: MutableState<Boolean?> = remember { mutableStateOf(null) }
    var isCorruptionHide : MutableState<Boolean?> = remember { mutableStateOf(null) }
    var depthValue by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(10.px)
            .background(color = Colors.DarkGrey),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier.width(400.px)
                .padding(20.px)
                .background(color = Colors.LightGrey)
                .borderRadius(16.px)
                .margin(bottom = 10.px),
        ) {
            Text("Этот сайт поможет вам опредлеить, какой метод контроля использовать для обнаружения деффектов.")
            Spacer()
            Text(" Просто задайте параметры и мы дадим вам рекомендации!")
        }

        Box(
            modifier = Modifier.width(400.px)
                .padding(20.px)
                .background(color = Colors.LightGrey)
                .borderRadius(16.px)
                .margin(bottom = 10.px),
        ) {

            Column {
                Text("Укажите глубину залегания")

                Row {
                    NumberInput(
                        min = 0,
                        max = 250,
                    ) {
                        defaultValue(depthValue)
                        onChange { event ->
                            event.value?.let { depthValue = it.toInt() }
                        }
                    }
                    Text("мм")

//                    (
//                            type = InputType.Number,
//                    ) {
//                    defaultValue(depthValue)
//                    onChange { event ->
//                        event.value?.let { depthValue = it.toInt() }
//                    }
//                }
                }
            }
        }

        when {
            depthValue == 0 -> {
                Dialog("Деффект скрыт от прямого обзора?", isCorruptionHide)
                isCorruptionHide.value?.let {
                    if(it) EndoTipBox()
                    else VisualTipBox()
                }
            }
            depthValue < 6 -> {
                MagnetTipBox()
            }
            depthValue < 25 -> {
                InductTipBox()
            }
            depthValue < 100 -> {
                Dialog("Совпадает с направлением просвечивания излучения?", isDirectionTheSame)
                isDirectionTheSame.value?.let {
                    if (it) UltraTipBox()
                    else RadicTipBox()
                }
            }
            depthValue < 250 -> {
                UltraTipBox()
            }

            else -> {
                TipBox("К сожалению, не удалось найти каких-либо рекомендаций")
            }
        }

//        PictureTip("visual/1.png", "Подрез - продольное углубление на наружной поверхности валика сварного шва")
    }
}

@Composable
fun PictureTipBox(
    src : String,
    description: String,
) {
    Box(
        modifier = Modifier.width(400.px)
            .padding(20.px)
            .margin(10.px)
            .borderRadius(16.px)
            .background(color = Colors.LightGray)
    ) {
        Image(
            src = src,
            modifier = Modifier.align(Alignment.TopCenter)
                .maxSize(350.px)
                .borderRadius(16.px)
        )
        Text(description)
    }
}

@Composable
fun VisualTipBox() {

    TipBox("Вам необходимо использовать визуальный метод контоля")

    Row {
        PictureTipBox("visual/1.png", "Подрез - продольное углубление на наружной поверхности валика сварного шва")
        PictureTipBox("visual/2.png", "Неравномерная ширина шва - отклонение ширины сварного шва от установленного значения")
        PictureTipBox("visual/3.png", "Кратер - усадочная раковина в конце валика сварного шва, не заваренная до или во время выполнения последующих проходов")
    }

    Row {
        PictureTipBox("visual/4.png", "Брызги металла - капли наплавленного или присадочного металла, образовавшиеся во время сварки и прилипшие к поверхности затвердевшего металла сварного шва или околошовной зоны основною металла.")
        PictureTipBox("visual/5.png", "Непровар - несплошность по всей длине шва или на его отдельном участке, возникающая из-за неспособности расплавленного металла проникнуть внутрь соединения.")
        PictureTipBox("visual/6.png", "Трещина -  несплошность, вызванная местным разрушением шва и его охлаждением, либо действием нагрузок.")
    }

    Row {
        PictureTipBox("visual/7.png", "Прожог - вытекание металла сварочной ванны, в результате чего образуется сквозное отверстие в сварном шве")
        PictureTipBox("visual/8.png", "Свищ - трубчатая полость в металле сварного шва из-за выделений газа.")
    }
}

@Composable
fun InductTipBox() {

    TipBox("Индукционный метод неразрушающего контроля - основным принципом данного метода является формирование электродвижущей силы с применением индукционных катушек.\n" +
            "Процедура исследования предполагает перемещение анализируемого изделия относительно индукционной катушки. \n" +
            "В процессе прохождения участка, имеющего дефект, происходит изменение магнитного потока, вызывающего электродвижущую силу индукции. \n" +
            "Он регистрируется измерительными приборами, что позволяет установить наличие дефекта.  По сути – это тот же гальванометр, соединенный с сигнальным индикатором.\n")

    Row {
        PictureTipBox("induct/1.png", "Визуализация проведения дефектоскопии")
        PictureTipBox("induct/2.png", "Визуализация принципа работы метода и результат")
    }
}

@Composable
fun MagnetTipBox() {

    TipBox("Магнитопорошковый метод неразрушающего контроля основан на явлении притяжения частиц магнитного порошка магнитными потоками рассеяния, \n" +
            "возникающими над дефектами в намагниченных объектах контроля.\n" +
            "Данный метод применяют для контроля объектов из ферромагнитных материалов с магнитными свойствами, \n" +
            "позволяющими создавать в местах нарушения сплошности магнитные поля рассеяния, достаточные для притяжения частиц магнитного порошка.\n" +
            "Метод может быть использован и для контроля объектов с немагнитными покрытиями.")

    Row {
        PictureTipBox("magnet/1.png", "Принцип работы")
        PictureTipBox("magnet/2.jpg", "Нанесение порошка и магнит")
        PictureTipBox("magnet/3.jpg", "Применение метода на трубе большого диаметра")
    }

    Row {
        PictureTipBox("magnet/4.png", "Трещины, выявленные методом")
        PictureTipBox("magnet/5.png", "Тоже")
    }
}

@Composable
fun RadicTipBox() {

    TipBox("Радиационный (радиографический) контроль (РК) – метод неразрушающего контроля, который позволяет выявить скрытые дефекты сварных стыков без их повреждения. \n" +
            "В основе методики – способность рентгеновских волн проходить через металл. \n" +
            "Излучение, которое выходит с обратной стороны деталей, может быть измерено. По полученным результатам судят о дефекте.")

    Row {
        PictureTipBox("radic/1.png", "Принцип работы и пример с дефектом внутри шва")
        PictureTipBox("radic/2.png", "Схема рентгеновской дефектоскопии")
        PictureTipBox("radic/3.jpg", "Трещина, обнаруженная данным методом")
    }

    Row {
        PictureTipBox("radic/4.png", "Пример дефектоскопии. Белая вертикальная линия - сварной шов. Тёмные участки на нём - различные дефекты.")
        PictureTipBox("radic/5.png", "Тоже")
        PictureTipBox("radic/6.png", "Оборудование. Основная часть прибора - это излучатель (изготовлен в виде сосуда). Пучки электронов, проходя через исследуемый обхект, формируют изображени на пленке")
    }
}

@Composable
fun UltraTipBox() {

    TipBox("Ультразвуковой метод контроля сварного шва - это метод неразрушающего контроля, позволяющего выявлять скрытые повреждения посредством ультразвука.\n" +
            "Принцип работы: на исследуемую деталь направляют ультразвуковые сигналы, которые, отражаясь от неровностей внутри шва, возвращаются в приемник. \n" +
            "Амплитуда отраженного импульса говорит о величине, время распространения волн - о глубине залегания.\n")

    Row {
        PictureTipBox("ultra/1.png", "Принцип работы")
        PictureTipBox("ultra/2.png", "Пример работы")

    }

    Row {
        PictureTipBox("ultra/3.png", "Фото применения")
        PictureTipBox("ultra/4.png", "Сам прибор. Сам прибор состоит из двух частей: дефектоскопа (излучает и поглощает волны) и пьезоэлектрического преобразователя, \n" +
                "(регистрирует их и трансформирует в электрические). Данные выводятся на электронный экран устройства.")
    }
}

@Composable
fun EndoTipBox() {

    TipBox("Эндоскоп (технический эндоскоп или бороскоп) – оптический прибор, который используется для визуального осмотра внутренних поверхностей трубок, отверстий или других труднодоступных мест. \n" +
            "Он позволяет заглядывать внутрь труднодоступных мест через имеющиеся технологические отверстия для определения наличия поверхностных дефектов.")

    Row {
        PictureTipBox("endo/1.png", "Эндоскоп и изображение с него")
        PictureTipBox("endo/2.png", "Просто эндоскоп ")
        PictureTipBox("endo/3.png", "Изображение с эндоскопа")
    }
}


@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun FourDialog(
    title : String,
    state : MutableState<DepthVariations?>
) {
    Box(
        modifier = Modifier.width(400.px)
            .padding(20.px)
            .background(color = Colors.LightGrey)
            .borderRadius(16.px)
            .margin(bottom = 10.px),

        ) {

        Column {
            Text(title)

            Column(
                modifier = Modifier.padding(10.px)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                RadioGroup(checkedValue = state.value.toString()) {

                    Row(
                        modifier = Modifier.width(200.px)
                    ) {
                        Text("До 6 мм")
                        RadioInput(value = DepthVariations.Lower6.name) { onClick { state.value = DepthVariations.Lower6 }  }
                    }

                    Row(
                        modifier = Modifier.width(200.px)
                    ) {
                        Text("От 6 мм до 25 мм")
                        RadioInput(value = DepthVariations.From6To25.name) { onClick { state.value = DepthVariations.From6To25 }  }
                    }

                    Row(
                        modifier = Modifier.width(200.px)
                    ) {
                        Text("От 25 мм до 100 мм")
                        RadioInput(value = DepthVariations.From25To100.name) { onClick { state.value = DepthVariations.From25To100 }  }
                    }

                    Row(
                        modifier = Modifier.width(200.px)
                    ) {
                        Text("От 100 мм до 250 мм")
                        RadioInput(value = DepthVariations.From100To250.name) { onClick { state.value = DepthVariations.From100To250 }  }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun Dialog(
    title : String,
    state : MutableState<Boolean?>
) {

    val value = state.value

    Box(
        modifier = Modifier.width(400.px)
            .padding(20.px)
            .background(color = Colors.LightGrey)
            .borderRadius(16.px)
            .margin(bottom = 10.px),

        ) {

        Column {
            Text(title)

            Row(
                modifier = Modifier.padding(10.px)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RadioGroup(checkedValue = value.toString()) {
                    Row(
                        modifier = Modifier.width(120.px)
                    ) {
                        Text("Да")
                        RadioInput(value = "true") { onClick { state.value = true }  }
                    }

                    Row(
                        modifier = Modifier.width(120.px)
                    ) {
                        Text("Нет")
                        RadioInput(value = "false") { onClick { state.value = false }  }
                    }
                }
            }
        }
    }

}


@Composable
fun TipBox(
    title : String
) {
    Box(
        modifier = Modifier.width(400.px)
            .padding(20.px)
            .borderRadius(16.px)
            .background(color = Colors.LightGray)
    ) {
        Text(title)
    }
}


enum class Results(
    val title: String,
    val pictureTips : List<PictureTip>,
) {
    Visual("Вам необходимо использовать визуальный метод контоля", listOf(
        PictureTip("visual/1.png", "Подрез - продольное углубление на наружной поверхности валика сварного шва"),
        PictureTip("visual/1.png", "Подрез - продольное углубление на наружной поверхности валика сварного шва"),
    ))
}

data class PictureTip(
    val src : String,
    val description: String,
)

enum class DepthVariations {
    Lower6, From6To25, From25To100, From100To250
}