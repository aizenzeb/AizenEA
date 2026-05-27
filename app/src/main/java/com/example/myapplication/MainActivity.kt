package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.AizenViewModel
import com.example.data.Mt5Account
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.DarkCarbon
import com.example.ui.theme.NeonCrimson
import com.example.ui.theme.DeepBloodRed
import com.example.ui.theme.PureWhite
import com.example.ui.theme.CoolGray
import com.example.ui.theme.HeavyMetallic
import com.example.ui.theme.SoftGlowCrimson
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.GoldGlow
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel: AizenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MatteBlack
                ) { innerPadding ->
                    AizenMainScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

enum class ActiveTab(val label: String, val icon: ImageVector) {
    TERMINAL("Terminal", Icons.Default.Home),
    COPILOT("AI Copilot", Icons.Default.Search),
    PORTFOLIOS("Portfolios", Icons.Default.Settings),
    ACADEMY("Academy", Icons.Default.Info)
}

@Composable
fun AizenMainScreen(viewModel: AizenViewModel, modifier: Modifier = Modifier) {
    var activeTab by remember { mutableStateOf(ActiveTab.TERMINAL) }
    val accounts by viewModel.accounts.collectAsState()
    val selectedAccount by viewModel.selectedAccount.collectAsState()
    val prices by viewModel.marketPrices.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MatteBlack, Color(0xFF040405))
                )
            )
    ) {
        // Luxury Decorum Top Header
        AizenTopBrandHeader()

        // Balance & Floating Equity Banner
        AizenFloatingEquityBanner(selectedAccount = selectedAccount)

        // Navigation Tabs (Apple-Level High-end Custom Segmented Bar)
        AizenSegmentedTabBar(
            selectedTab = activeTab,
            onTabSelected = { activeTab = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Main Tab Content Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            when (activeTab) {
                ActiveTab.TERMINAL -> TerminalTabContent(viewModel, prices)
                ActiveTab.COPILOT -> CopilotTabContent(viewModel)
                ActiveTab.PORTFOLIOS -> PortfoliosTabContent(viewModel, accounts, selectedAccount)
                ActiveTab.ACADEMY -> AcademyTabContent()
            }
        }
    }
}

@Composable
fun AizenTopBrandHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Glowing Custom Crimson Emblem with blur radial halo
            Box(
                modifier = Modifier
                    .size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Blur Halo (Simulating CSS blur-md bg-red-600)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .drawBehind {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(NeonCrimson.copy(alpha = 0.5f), Color.Transparent),
                                    center = center,
                                    radius = size.minDimension / 1.5f
                                )
                            )
                        }
                )
                // Inner Shining Dot (Simulating z-10 w-3 h-3 bg-red-500 shadow)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(NeonCrimson)
                        .drawBehind {
                            drawCircle(
                                color = PureWhite,
                                radius = 1.5.dp.toPx()
                            )
                        }
                )
            }

            // Bold italicized Aizen + NeonCrimson EA logotext
            val annotatedBrand = buildAnnotatedString {
                withStyle(style = SpanStyle(color = PureWhite, fontWeight = FontWeight.Black, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                    append("AIZEN")
                }
                withStyle(style = SpanStyle(color = NeonCrimson, fontWeight = FontWeight.Black, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                    append("EA")
                }
            }
            Text(
                text = annotatedBrand,
                fontSize = 20.sp,
                letterSpacing = (-0.5).sp
            )
        }

        // Live Operational State Glass Pill (Tailwind: `bg-white/5 border border-white/10 rounded-full px-3 py-1 flex items-center gap-2`)
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val statusAlpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "status_alpha"
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0x11FFFFFF))
                .border(1.dp, Color(0x19FFFFFF), RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(CyberGreen.copy(alpha = statusAlpha))
            )
            Text(
                text = "MT5 LIVE",
                color = PureWhite.copy(alpha = 0.7f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )
        }
    }
}

@Composable
fun AizenFloatingEquityBanner(selectedAccount: Mt5Account?) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    
    val acc = selectedAccount
    if (acc != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x13FFFFFF), Color(0x02FFFFFF)),
                        center = Offset(0f, 0f),
                        radius = 800f
                    )
                )
                .border(1.dp, Color(0x19FFFFFF), RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            // Header row with Alias and Node Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${acc.aliasName} [${acc.accountGroup}]",
                    color = PureWhite.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Inst. Node #412",
                    color = PureWhite.copy(alpha = 0.3f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Total Equity",
                color = PureWhite.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // Large elegant light-weight currency display
            val formattedEquity = currencyFormat.format(acc.equity)
            val parts = formattedEquity.split(".")
            val mainPart = parts.getOrNull(0) ?: "$0"
            val fractionPart = parts.getOrNull(1) ?: "00"
            
            val profit = acc.equity - acc.balance
            val isProfit = profit >= 0
            val profitColor = if (isProfit) CyberGreen else NeonCrimson
            val profitPercent = if (acc.balance > 0) (profit / acc.balance) * 100 else 0.0
            
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = mainPart,
                        color = PureWhite,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = ".$fractionPart",
                        color = PureWhite.copy(alpha = 0.4f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                }
                
                // Small percentage tag
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(profitColor.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = String.format(Locale.US, "${if (isProfit) "+" else ""}%.2f%%", profitPercent),
                        color = profitColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0x0EFFFFFF))
            )
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // Metrics Triple Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Column 1
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Margin Level",
                        color = PureWhite.copy(alpha = 0.4f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1850.4%",
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Column 2
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Open Risk",
                        color = PureWhite.copy(alpha = 0.4f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "0.50%",
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Column 3
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Drawdown",
                        color = PureWhite.copy(alpha = 0.4f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "2.1%",
                        color = NeonCrimson,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x13FFFFFF), Color(0x02FFFFFF))
                    )
                )
                .border(1.dp, Color(0x19FFFFFF), RoundedCornerShape(20.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NO PORTFOLIO SECURED. REGISTER ACCOUNT BELOW.",
                color = CoolGray,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun AizenSegmentedTabBar(selectedTab: ActiveTab, onTabSelected: (ActiveTab) -> Unit) {
    ScrollableTabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = Color.Transparent,
        contentColor = NeonCrimson,
        edgePadding = 16.dp,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = NeonCrimson,
                height = 2.dp
            )
        }
    ) {
        ActiveTab.values().forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier.size(16.dp),
                            tint = if (selectedTab == tab) NeonCrimson else CoolGray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tab.label,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == tab) PureWhite else CoolGray,
                            letterSpacing = 1.sp
                        )
                    }
                }
            )
        }
    }
}

// ======================== TAB 1: CORE TERMINAL ========================

@Composable
fun TerminalTabContent(viewModel: AizenViewModel, prices: Map<String, Double>) {
    var activeAsset by remember { mutableStateOf("EURUSD") }
    val historyMap by viewModel.chartHistory.collectAsState()
    val listState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(listState)
    ) {
        // Horizontal assets picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            prices.forEach { (symbol, price) ->
                val isActive = activeAsset == symbol
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isActive) Color(0x33FF2E2E) else HeavyMetallic)
                        .border(
                            1.dp,
                            if (isActive) NeonCrimson else Color.Transparent,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { activeAsset = symbol }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .widthIn(min = 90.dp)
                ) {
                    Column {
                        Text(
                            text = symbol,
                            color = if (isActive) PureWhite else CoolGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(
                                if (symbol == "BTCUSD" || symbol == "US30" || symbol == "XAUUSD") "%.2f" else "%.5f",
                                price
                            ),
                            color = if (isActive) NeonCrimson else PureWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Real-time Live Rendered Canvas Line Chart
        val currentHistory = historyMap[activeAsset] ?: emptyList()
        AizenLiveTickingChartCard(symbol = activeAsset, history = currentHistory)

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Confluence Analyzer Framework & OTE Projections
        AizenTacticalConfluenceDashboard(viewModel)
        
        Spacer(modifier = Modifier.height(16.dp))

        // Open Positions Real-time Profit list
        AizenPositionsPanel(viewModel)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AizenLiveTickingChartCard(symbol: String, history: List<Double>) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse),
        label = "shimmer"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                )
            )
            .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Chart",
                    tint = NeonCrimson,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$symbol 1-MIN COGNITIVE TICK CHART",
                    color = PureWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(NeonCrimson.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "LIVE SPREAD",
                    color = NeonCrimson,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Canvas Drawing Block for 120FPS Ticking Graph
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .drawBehind {
                        // Drawing professional carbon lines
                        val verticalDividerCount = 5
                        for (i in 1..verticalDividerCount) {
                            val x = size.width * (i.toFloat() / (verticalDividerCount + 1))
                            drawLine(
                                color = Color(0x0FFF2E2E),
                                start = Offset(x, 0f),
                                end = Offset(x, size.height),
                                strokeWidth = 1f
                            )
                        }
                        val horizontalDividerCount = 3
                        for (i in 1..horizontalDividerCount) {
                            val y = size.height * (i.toFloat() / (horizontalDividerCount + 1))
                            drawLine(
                                color = Color(0x0FFF2E2E),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 1f
                            )
                        }
                    }
            ) {
                if (history.size > 1) {
                    val maxVal = history.maxOrNull() ?: 1.0f.toDouble()
                    val minVal = history.minOrNull() ?: 0.0f.toDouble()
                    val valRange = maxVal - minVal

                    val points = mutableListOf<Offset>()
                    val stepX = size.width / (history.size - 1)

                    history.forEachIndexed { index, price ->
                        val x = index * stepX
                        // Normalize price inside visual bounds
                        val ratio = if (valRange > 0.0) (price - minVal) / valRange else 0.5
                        val y = size.height - (ratio * (size.height * 0.8f) + (size.height * 0.1f)).toFloat()
                        points.add(Offset(x, y))
                    }

                    // Draw Gradient Area Sweep Below Line
                    val gradientPath = Path().apply {
                        moveTo(0f, size.height)
                        points.forEach { point ->
                            lineTo(point.x, point.y)
                        }
                        lineTo(size.width, size.height)
                        close()
                    }
                    drawPath(
                        path = gradientPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(NeonCrimson.copy(alpha = 0.2f), Color.Transparent)
                        )
                    )

                    // Draw actual Crimson Line
                    val linePath = Path().apply {
                        val first = points.first()
                        moveTo(first.x, first.y)
                        points.drop(1).forEach { point ->
                            lineTo(point.x, point.y)
                        }
                    }
                    drawPath(
                        path = linePath,
                        color = NeonCrimson,
                        style = Stroke(width = 4f)
                    )

                    // Pulse glow dot on latest tick
                    val latest = points.last()
                    drawCircle(
                        color = NeonCrimson,
                        radius = 6f,
                        center = latest
                    )
                    drawCircle(
                        color = NeonCrimson.copy(alpha = shimmerAlpha),
                        radius = 16f,
                        center = latest
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Info Summary showing EMA confluences on charts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Fast EMA: 9 [Active]",
                color = GoldGlow,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Slow EMA: 21 [Active]",
                color = NeonCrimson,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Trend EMA: 50 [Active]",
                color = CoolGray,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun AizenPositionsPanel(viewModel: AizenViewModel) {
    val positions by viewModel.openPositions.collectAsState()
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                )
            )
            .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Positions",
                    tint = NeonCrimson,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ACTIVE FLOATING TRADES (AUTO COPIED)",
                    color = PureWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF1B1B1E))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${positions.size} RUNNING",
                    color = CoolGray,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (positions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No open positions on active matrix.",
                    color = CoolGray,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                positions.forEach { pos ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(HeavyMetallic)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (pos.isBuy) CyberGreen.copy(alpha = 0.15f) else NeonCrimson.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (pos.isBuy) "BUY" else "SELL",
                                    color = if (pos.isBuy) CyberGreen else NeonCrimson,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${pos.lots} lots — ${pos.pair}",
                                    color = PureWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Entry: ${String.format("%.5f", pos.entryPrice)} → Price: ${String.format("%.5f", pos.currentPrice)}",
                                    color = CoolGray,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Text(
                            text = currencyFormat.format(pos.floatingProfit),
                            color = if (pos.floatingProfit >= 0) CyberGreen else NeonCrimson,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AizenTacticalConfluenceDashboard(viewModel: AizenViewModel) {
    // Collect settings
    val ema by viewModel.emaAligned.collectAsState()
    val pivot by viewModel.pivotBouncing.collectAsState()
    val rsi by viewModel.rsiInZone.collectAsState()
    val bb by viewModel.bbExpanding.collectAsState()
    val htf by viewModel.htfBiasAligned.collectAsState()
    val lq by viewModel.liquiditySwept.collectAsState()
    val kz by viewModel.sessionKillZone.collectAsState()
    val m5 by viewModel.m5StructureShift.collectAsState()
    val qt by viewModel.quarterlyQ3.collectAsState()

    val score = viewModel.getConfluenceScore()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                )
            )
            .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Confluence",
                    tint = NeonCrimson,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AizenEA TACTICAL CONFLUENCE MATRIX",
                    color = PureWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Enforce all parameters for high probability institutional setups. Discard if key structure fails.",
            color = CoolGray,
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Grid-based toggles for confluences
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ConfluenceToggleItem("EMA Trend [9>21>50]", ema) { viewModel.toggleEma() }
                ConfluenceToggleItem("Pivot Bounce [S/R Interaction]", pivot) { viewModel.togglePivot() }
                ConfluenceToggleItem("RSI Momentum [Bull/Bear zone]", rsi) { viewModel.toggleRsi() }
                ConfluenceToggleItem("Bands Volatility [Expanding]", bb) { viewModel.toggleBb() }
                ConfluenceToggleItem("HTF Daily Orderflow Alignment", htf) { viewModel.toggleHtf() }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ConfluenceToggleItem("Turtle Soup [Liquidity Taken]", lq) { viewModel.toggleSweep() }
                ConfluenceToggleItem("Kill Zone Session Time Window", kz) { viewModel.toggleKillZone() }
                ConfluenceToggleItem("M5 Structure Shift (MSS)", m5) { viewModel.toggleM5Shift() }
                ConfluenceToggleItem("Quarterly Theory [Q3 Distribution]", qt) { viewModel.toggleQuarter() }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Score display matching hedge-fund parameters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(HeavyMetallic)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(if (score >= 8) NeonCrimson.copy(alpha = 0.2f) else Color(0x333A3A3C))
                        .border(
                            1.dp,
                            if (score >= 8) NeonCrimson else CoolGray,
                            RoundedCornerShape(19.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$score/9",
                        color = if (score >= 8) NeonCrimson else PureWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "SOVEREIGN ALIGNMENT",
                        color = PureWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = when (score) {
                            9 -> "SOVEREIGN HIGH-PROBABILITY ENTRY APPROVED"
                            7, 8 -> "MODERATE CONFLUENCE — RETEST SUGGESTED"
                            else -> "DISQUALIFIED PROTOCOL: NO TRADE PROFILE"
                        },
                        color = if (score >= 8) CyberGreen else if (score >= 6) GoldGlow else NeonCrimson,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Dynamic OTE projection zones calculator
        Text(
            text = "OPTIMAL TRADE ENTRY (OTE) FIB ENGINE",
            color = PureWhite,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.5.sp
        )
        Text(
            text = "Simulated dealing range: High (1.0850) to Low (1.0790) | Equilibrium: 1.0820",
            color = CoolGray,
            fontSize = 9.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OteZoneCard("62% Premium", "1.0827", false)
            OteZoneCard("70.5% OTE", "1.0832", true)
            OteZoneCard("79% Discount", "1.0803", false)
        }
    }
}

@Composable
fun ConfluenceToggleItem(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF0F0F10))
            .clickable { onToggle() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = if (checked) PureWhite else CoolGray,
            fontSize = 10.sp,
            fontWeight = if (checked) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(if (checked) NeonCrimson else Color(0xFF2C2C2E))
                .border(
                    1.dp,
                    if (checked) NeonCrimson else Color(0x669E9EA4),
                    RoundedCornerShape(3.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "checked",
                    tint = PureWhite,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

@Composable
fun RowScope.OteZoneCard(label: String, value: String, isHot: Boolean) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isHot) Color(0x33FF2E2E) else HeavyMetallic)
            .border(
                1.dp,
                if (isHot) NeonCrimson else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                color = if (isHot) NeonCrimson else CoolGray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = PureWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// ======================== TAB 2: AI COPILOT ========================

@Composable
fun CopilotTabContent(viewModel: AizenViewModel) {
    val currentPair by viewModel.selectedPair.collectAsState()
    val currentTf by viewModel.selectedTimeframe.collectAsState()
    val currentTheory by viewModel.selectedTheory.collectAsState()
    val currentGeopoliticalNews by viewModel.geopoliticalInput.collectAsState()
    
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val reportOutput by viewModel.analysisReport.collectAsState()

    val pairs = listOf("EURUSD", "GBPUSD", "XAUUSD", "BTCUSD", "DXY", "US30")
    val timeframes = listOf("M5", "M15", "H1", "H4", "DAILY")
    val strategies = listOf(
        "Quarterly Theory + AMD",
        "Candle Range Theory (CRT)",
        "Turtle Soup & Liquidity Sweeps",
        "MMXM Buy/Sell Models",
        "Volume Profile + OTE",
        "SK Fibonacci System"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dropdown settings block
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                        )
                    )
                    .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "brain",
                        tint = NeonCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "QUANT MATRIX COGNITIVE CONTROL",
                        color = PureWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Selector: Pair
                Text("INTELLIGENCE PAIR TARGET", color = CoolGray, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    pairs.forEach { p ->
                        val selected = p == currentPair
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) NeonCrimson else HeavyMetallic)
                                .clickable { viewModel.setPair(p) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(p, color = PureWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Selector: Timeframe & Theory focus
                Text("ANALYSIS FRACTAL RANGE", color = CoolGray, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    timeframes.forEach { tf ->
                        val selected = tf == currentTf
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) NeonCrimson else HeavyMetallic)
                                .clickable { viewModel.setTimeframe(tf) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(tf, color = PureWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Selected Concept Dropdown
                Text("SMC STRATEGY THEORY FOCUS", color = CoolGray, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    strategies.forEach { st ->
                        val selected = st == currentTheory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) Color(0x33FF2E2E) else HeavyMetallic)
                                .border(1.dp, if (selected) NeonCrimson else Color.Transparent, RoundedCornerShape(6.dp))
                                .clickable { viewModel.setTheory(st) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(st, color = if (selected) NeonCrimson else PureWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Text Input: Geopolitical news statement
                Text("CENTRAL BANK SPEECH / GEOPOLITICAL NEWS STATEMENT", color = CoolGray, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = currentGeopoliticalNews,
                    onValueChange = { viewModel.setGeopoliticalInput(it) },
                    modifier = Modifier.fillMaxWidth().testTag("news_input"),
                    textStyle = TextStyle(color = PureWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCrimson,
                        unfocusedBorderColor = Color(0xFF333336),
                        focusedContainerColor = Color(0xFF0F0F10),
                        unfocusedContainerColor = Color(0xFF09090A)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Trigger Button
                Button(
                    onClick = { viewModel.runMatrixAnalysis() },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("analyze_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCrimson),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(color = PureWhite, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("DECRYPTION IN TRANSMISSION...", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "flash", tint = PureWhite, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("RUN QUANT COGNITIVE ANALYSIS", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Markdown Report Visualizer Block
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                        )
                    )
                    .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "QUANT ANALYSIS MATRIX REPORT",
                        color = PureWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF140707))
                            .border(1.dp, Color(0x55FF2E2E), RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = "Aizen Core V1.5",
                            color = NeonCrimson,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Custom Styled Markdown Parser View to ensure elite Apple-level aesthetics
                AizenMarkdownDisplay(reportOutput)
            }
        }
        
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AizenMarkdownDisplay(rawText: String) {
    if (rawText.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Sovereign analytical parameters ready. Standby.", color = CoolGray, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        }
    } else {
        val parsedLines = rawText.split("\n")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            parsedLines.forEach { line ->
                val trimmed = line.trim()
                when {
                    trimmed.startsWith("###") -> {
                        Text(
                            text = trimmed.replace("###", "").trim(),
                            color = NeonCrimson,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    trimmed.startsWith("####") -> {
                        Text(
                            text = trimmed.replace("####", "").trim(),
                            color = GoldGlow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    trimmed.startsWith("**") && trimmed.endsWith("**") -> {
                        Text(
                            text = trimmed.replace("**", "").trim(),
                            color = PureWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    trimmed.startsWith("*") || trimmed.startsWith("-") -> {
                        // Styled bullet
                        val bulletContent = trimmed.substring(1).trim()
                        val annotatedText = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = NeonCrimson, fontWeight = FontWeight.Bold)) {
                                append(" • ")
                            }
                            appendParsedBoldText(bulletContent)
                        }
                        Text(
                            text = annotatedText,
                            color = CoolGray,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    else -> {
                        if (trimmed.isNotEmpty()) {
                            Text(
                                text = buildAnnotatedString { appendParsedBoldText(trimmed) },
                                color = CoolGray,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

fun AnnotatedString.Builder.appendParsedBoldText(text: String) {
    val regex = "\\*\\*(.*?)\\*\\*".toRegex()
    var lastIndex = 0
    regex.findAll(text).forEach { matchResult ->
        // Append text before match
        append(text.substring(lastIndex, matchResult.range.first))
        // Append bold text
        withStyle(style = SpanStyle(color = PureWhite, fontWeight = FontWeight.Bold)) {
            append(matchResult.groupValues[1])
        }
        lastIndex = matchResult.range.last + 1
    }
    if (lastIndex < text.length) {
        append(text.substring(lastIndex))
    }
}

// ======================== TAB 3: PORTFOLIOS ========================

@Composable
fun PortfoliosTabContent(
    viewModel: AizenViewModel,
    accounts: List<Mt5Account>,
    selectedAccount: Mt5Account?
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val logs by viewModel.copierLogs.collectAsState()
    val listState = rememberScrollState()

    if (showAddDialog) {
        AddAccountDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { login, name, broker, group, leverage, initBalance, type ->
                viewModel.addNewMt5Account(login, name, broker, group, leverage, initBalance, type)
                showAddDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(listState)
    ) {
        // Multi Accounts Picker Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                    )
                )
                .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SECURE MT5 ACCOUNT MANAGEMENTS",
                    color = PureWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonCrimson)
                        .clickable { showAddDialog = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = PureWhite, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SECURE NEW", color = PureWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (accounts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No secure portfolios saved locally.", color = CoolGray, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    for (acc in accounts) {
                        val isSelected = selectedAccount?.id == acc.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0x22FF2E2E) else HeavyMetallic)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCrimson else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.selectAccount(acc) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(acc.aliasName, color = PureWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (acc.type == "INSTITUTIONAL") Color(0xFF1B0715) else Color(0xFF07141B))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = acc.type,
                                            color = if (acc.type == "INSTITUTIONAL") Color(0xFFFF4CFB) else Color(0xFF4CFFFB),
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Broker: ${acc.broker} | Login: ${acc.login} | Leverage 1:${acc.leverage}",
                                    color = CoolGray,
                                    fontSize = 10.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Win: ${acc.winRatePercent}%",
                                        color = CyberGreen,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Trades: ${acc.totalTrades}",
                                        color = CoolGray,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(10.dp))

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = NeonCrimson,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { viewModel.removeAccount(acc) }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Account Copier Controller Panel
        val copierAcc = selectedAccount
        if (copierAcc != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                        )
                    )
                    .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI TRADE COPIER EXECUTIONS",
                        color = PureWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Switch(
                        checked = copierAcc.isCopierActive,
                        onCheckedChange = { active -> viewModel.toggleCopier(copierAcc.id, active) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PureWhite,
                            checkedTrackColor = NeonCrimson,
                            uncheckedThumbColor = CoolGray,
                            uncheckedTrackColor = Color(0xFF1B1B1E)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "If enabled, our institutional engine copies elite trade metrics seamlessly across secured MT5 accounts.",
                    color = CoolGray,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive Logs feed
                Text(
                    text = "COPIER REAL-TIME SYNC FEED (120FPS ROUTING)",
                    color = PureWhite,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (logs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Awaiting trade copier signals in active session...", color = CoolGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (log in logs) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(HeavyMetallic)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = log.action,
                                        color = PureWhite,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "From: ${log.sourceAccount} → To: ${log.targetAccount}",
                                        color = CoolGray,
                                        fontSize = 8.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = log.timestamp,
                                        color = CoolGray,
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(CyberGreen.copy(alpha = 0.15f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(log.status, color = CyberGreen, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AddAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        login: String,
        name: String,
        broker: String,
        group: String,
        leverage: Int,
        balance: Double,
        type: String
    ) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var broker by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("SMC Core") }
    var leverage by remember { mutableStateOf("100") }
    var balance by remember { mutableStateOf("100000") }
    var type by remember { mutableStateOf("LIVE") }

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F11)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0x44FF1A1A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "SECURE METATRADER 5 LINK",
                    color = PureWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Encrypted key tunnels protect credentials in local database storage according to top security protocols.",
                    color = CoolGray,
                    fontSize = 9.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Fields
                AizenField("Account Login UID", login, { login = it }, KeyboardType.Number, "login_field")
                AizenField("Alias Portfolio Name", alias, { alias = it }, KeyboardType.Text, "alias_field")
                AizenField("Broker Target Server", broker, { broker = it }, KeyboardType.Text, "broker_field")
                AizenField("Default Group (Tag)", group, { group = it }, KeyboardType.Text, "group_field")
                AizenField("Account Leverage (e.g. 100 for 1:100)", leverage, { leverage = it }, KeyboardType.Number, "leverage_field")
                AizenField("Starting Capital (USD)", balance, { balance = it }, KeyboardType.Number, "balance_field")

                // Option Row: LIVE / DEMO / INSTITUTIONAL
                Text("ACCOUNT CLEARANCE TYPE", color = CoolGray, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("LIVE", "DEMO", "INSTITUTIONAL").forEach { opt ->
                        val selected = type == opt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) NeonCrimson else HeavyMetallic)
                                .clickable { type = opt }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(opt, color = PureWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CoolGray),
                        border = BorderStroke(1.dp, Color(0xFF333336))
                    ) {
                        Text("CANCEL", style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp))
                    }

                    Button(
                        onClick = {
                            if (login.isEmpty() || alias.isEmpty() || broker.isEmpty()) {
                                Toast.makeText(context, "SMC Protocol dictates all fields must be secure.", Toast.LENGTH_SHORT).show()
                            } else {
                                val lev = leverage.toIntOrNull() ?: 100
                                val bal = balance.toDoubleOrNull() ?: 100000.0
                                onConfirm(login, alias, broker, group, lev, bal, type)
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("save_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCrimson)
                    ) {
                        Text("ESTABLISH", style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = PureWhite))
                    }
                }
            }
        }
    }
}

@Composable
fun AizenField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    testTag: String
) {
    Column {
        Text(label.uppercase(Locale.ROOT), color = CoolGray, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag(testTag),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(color = PureWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCrimson,
                unfocusedBorderColor = Color(0xFF333336),
                focusedContainerColor = Color(0xFF0F0F10),
                unfocusedContainerColor = Color(0xFF09090A)
            ),
            shape = RoundedCornerShape(6.dp),
            singleLine = true
        )
    }
}

// ======================== TAB 4: ACADEMY ========================

@Composable
fun AcademyTabContent() {
    val items = listOf(
        AcademyItem(
            title = "FVG — Fair Value Gap",
            description = "A Fair Value Gap is an imbalance in price delivery created when aggressive institutional orders cause candles to move so quickly that wicks do not overlap, leaving structural voids.",
            conceptQuote = "Bullish: wick of candle 1 high does not touch candle 3 low.\nBearish: wick of candle 1 low does not touch candle 3 high.",
            diagramType = "FVG"
        ),
        AcademyItem(
            title = "Quarterly Theory",
            description = "Time-based temporal framework that divides any trading candle (Daily, Weekly, Hourly) into 4 distinct quadrants driving institutional distribution flow cycles.",
            conceptQuote = "Q1: Accumulation (Range builds)\nQ2: Manipulation (Stop hunt false break)\nQ3: Distribution (Violent displacement/entry)\nQ4: Continuation or rebalancing.",
            diagramType = "QUARTER"
        ),
        AcademyItem(
            title = "CRT — Candle Range Theory",
            description = "Liquidity sweep strategy based on previous candle ranges. Institutions engineer sweeps below/above previous high/lows inside session volume before reversal.",
            conceptQuote = "Step 1: Sweep previous candle high/low\nStep 2: Close back inside prior range\nStep 3: Hunt lower timeframe Market Shift.",
            diagramType = "CRT"
        ),
        AcademyItem(
            title = "MSS — Market Structure Shift",
            description = "Aggressive structural transition marking structural breakage plus severe momentum displacement. Confirms institutional direction changes.",
            conceptQuote = "Usually combined with an active FVG formation inside execution timeframe (M5 / M15) after sweeps.",
            diagramType = "MSS"
        ),
        AcademyItem(
            title = "Turtle Soup & AMD Core",
            description = "Classical false breakout liquidity absorption model. High-probability setups form when equal highs or lows are swept inside New York/London Kill Zones.",
            conceptQuote = "Accumulation (Asian range) → Manipulation (London Sweep) → Distribution (NY Repricing).",
            diagramType = "AMD"
        )
    )

    var currentFocusIndex by remember { mutableStateOf(0) }
    val focusObj = items[currentFocusIndex]

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Concept Drawer Swapper
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                        )
                    )
                    .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "academy",
                        tint = NeonCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AIZENEA INSTITUTIONAL CRYPTEX",
                        color = PureWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))

                // horizontal conceptual selector
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items.forEachIndexed { idx, item ->
                        val active = idx == currentFocusIndex
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (active) Color(0x33FF2E2E) else HeavyMetallic)
                                .border(1.dp, if (active) NeonCrimson else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { currentFocusIndex = idx }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = item.title.split(" — ").first(),
                                color = if (active) NeonCrimson else PureWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Active Interactive Diagram Canvas Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F0F11), Color(0xFF070708))
                        )
                    )
                    .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "CONCEPTUAL DIAGRAM: ${focusObj.title.uppercase(Locale.ROOT)}",
                    color = PureWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(14.dp))

                // DRAW THE DIAGRAM
                AizenAcademyDiagram(focusObj.diagramType)

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = focusObj.description,
                    color = CoolGray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(HeavyMetallic)
                        .padding(12.dp)
                ) {
                    Text(
                        text = focusObj.conceptQuote,
                        color = PureWhite,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 14.sp
                    )
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

data class AcademyItem(
    val title: String,
    val description: String,
    val conceptQuote: String,
    val diagramType: String
)

@Composable
fun AizenAcademyDiagram(type: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF09090A)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (type) {
                "FVG" -> {
                    // Draw 3-Candle FVG imbalance
                    val cw = 30.dp.toPx()
                    val gapX = 40.dp.toPx()

                    // Candle 1: Big Green
                    val x1 = size.width / 2 - cw - gapX
                    drawRect(Color(0xFF1B4021), Offset(x1, 20.dp.toPx()), Size(cw, 80.dp.toPx()))
                    drawLine(Color(0xFF2E7D32), Offset(x1 + cw/2, 10.dp.toPx()), Offset(x1 + cw/2, 110.dp.toPx()), 2.dp.toPx())

                    // Candle 2: Hyper Displacement Candle (leaves gap)
                    val x2 = size.width / 2 - cw/2
                    drawRect(Color(0xFFFF2E2E), Offset(x2, 10.dp.toPx()), Size(cw, 115.dp.toPx()))
                    drawLine(Color(0xFFFF5252), Offset(x2 + cw/2, 5.dp.toPx()), Offset(x2 + cw/2, 130.dp.toPx()), 2.dp.toPx())

                    // Candle 3: Small green
                    val x3 = size.width / 2 + cw/2 + gapX
                    drawRect(Color(0xFF1B4021), Offset(x3, 70.dp.toPx()), Size(cw, 50.dp.toPx()))
                    drawLine(Color(0xFF2E7D32), Offset(x3 + cw/2, 60.dp.toPx()), Offset(x3 + cw/2, 125.dp.toPx()), 2.dp.toPx())

                    // Highlight the FVG void zone in golden dashed outline
                    val topGap = 100.dp.toPx() // Candle 1 high wick
                    val bottomGap = 60.dp.toPx() // Candle 3 low wick
                    drawRect(
                        color = GoldGlow.copy(alpha = 0.15f),
                        topLeft = Offset(x1 + cw, 30.dp.toPx()),
                        size = Size(x3 - (x1 + cw), 40.dp.toPx())
                    )
                    drawRect(
                        color = GoldGlow,
                        topLeft = Offset(x1 + cw, 30.dp.toPx()),
                        size = Size(x3 - (x1 + cw), 40.dp.toPx()),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
                "QUARTER" -> {
                    // Draw 4 Quarters timeline
                    val segmentW = size.width / 4
                    val h = size.height

                    // Draw segments
                    for (i in 0..3) {
                        val color = when(i) {
                            0 -> Color(0xFF625B71)
                            1 -> NeonCrimson
                            2 -> CyberGreen
                            else -> GoldGlow
                        }
                        val txt = when(i) {
                            0 -> "Q1: ACCUM"
                            1 -> "Q2: MANIP"
                            2 -> "Q3: DISTRIB"
                            else -> "Q4: EXPAND"
                        }
                        drawRect(
                            color.copy(alpha = 0.08f),
                            Offset(i * segmentW, 0f),
                            Size(segmentW, h)
                        )
                        drawLine(
                            Color(0x33FF2E2E),
                            Offset(i * segmentW, 0f),
                            Offset(i * segmentW, h),
                            1.dp.toPx()
                        )
                    }

                    // A hypothetical price curve moving across quarters
                    val path = Path().apply {
                        moveTo(0f, h/2) // Q1 sideways
                        lineTo(segmentW * 0.8f, h/2 + 5.dp.toPx())
                        lineTo(segmentW, h/2 - 5.dp.toPx())
                        // Q2: Manipulation sweep low
                        lineTo(segmentW * 1.5f, h - 20.dp.toPx())
                        // Q3: Strong distribution expansion up
                        lineTo(segmentW * 2.5f, 20.dp.toPx())
                        // Q4: High consolidation continuation
                        lineTo(size.width, 30.dp.toPx())
                    }
                    drawPath(path, NeonCrimson, style = Stroke(width = 2.dp.toPx()))
                }
                "CRT" -> {
                    // Draw yesterday's candle and today's sweeping candle
                    val cw = 40.dp.toPx()
                    val center = size.width / 2

                    // Previous Candle (Black body, large wicks)
                    val x1 = center - cw - 20.dp.toPx()
                    drawRect(Color(0xFF2C2C2E), Offset(x1, 40.dp.toPx()), Size(cw, 60.dp.toPx()))
                    drawLine(PureWhite, Offset(x1 + cw/2, 20.dp.toPx()), Offset(x1 + cw/2, 110.dp.toPx()), 2.dp.toPx())

                    // Sweeping Candle (sweeps low, closes inside)
                    val x2 = center + 20.dp.toPx()
                    drawRect(NeonCrimson.copy(alpha = 0.3f), Offset(x2, 45.dp.toPx()), Size(cw, 50.dp.toPx()))
                    drawRect(NeonCrimson, Offset(x2, 45.dp.toPx()), Size(cw, 50.dp.toPx()), style = Stroke(width = 1.dp.toPx()))
                    // Extremely long downward wick sweeping previous low
                    drawLine(NeonCrimson, Offset(x2 + cw/2, 25.dp.toPx()), Offset(x2 + cw/2, 125.dp.toPx()), 2.5.dp.toPx())

                    // Draw red dotted liquidity line of previous low
                    drawLine(
                        color = Color(0xFFFF2525),
                        start = Offset(x1 - 10.dp.toPx(), 110.dp.toPx()),
                        end = Offset(x2 + cw + 10.dp.toPx(), 110.dp.toPx()),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
                "MSS" -> {
                    // Break of structure line chart
                    val h = size.height
                    val w = size.width

                    val path = Path().apply {
                        moveTo(w * 0.1f, h * 0.7f)
                        lineTo(w * 0.3f, h * 0.4f) // high
                        lineTo(w * 0.45f, h * 0.85f) // low
                        lineTo(w * 0.65f, h * 0.3f) // new higher high
                        // Shift/displace down breaking prior structural low
                        lineTo(w * 0.85f, h * 0.95f)
                    }
                    drawPath(path, Color.White, style = Stroke(width = 2.dp.toPx()))

                    // Prior low level line
                    drawLine(
                        color = NeonCrimson,
                        start = Offset(w * 0.45f, h * 0.85f),
                        end = Offset(w * 0.9f, h * 0.85f),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
                else -> {
                    // Draw AMD generic curve
                    val h = size.height
                    val w = size.width

                    val path = Path().apply {
                        moveTo(0f, h/2)
                        // Accumulate
                        lineTo(w * 0.25f, h/2)
                        // Manipulate low
                        lineTo(w * 0.4f, h - 15.dp.toPx())
                        // Distribute high
                        lineTo(w * 0.75f, 20.dp.toPx())
                        lineTo(w, 25.dp.toPx())
                    }
                    drawPath(path, CyberGreen, style = Stroke(width = 2.5.dp.toPx()))
                }
            }
        }
    }
}
