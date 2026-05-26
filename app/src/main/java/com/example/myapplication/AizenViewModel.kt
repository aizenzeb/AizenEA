package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AizenDatabase
import com.example.data.GeminiService
import com.example.data.Mt5Account
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class AizenViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AizenDatabase.getDatabase(application)
    private val dao = db.mt5AccountDao()

    // MT5 Accounts List Flow
    private val _accounts = MutableStateFlow<List<Mt5Account>>(emptyList())
    val accounts: StateFlow<List<Mt5Account>> = _accounts.asStateFlow()

    // Selected MT5 Account
    private val _selectedAccount = MutableStateFlow<Mt5Account?>(null)
    val selectedAccount: StateFlow<Mt5Account?> = _selectedAccount.asStateFlow()

    // Real-Time Ticking Market Prices
    private val _marketPrices = MutableStateFlow<Map<String, Double>>(
        mapOf(
            "EURUSD" to 1.0825,
            "GBPUSD" to 1.2740,
            "XAUUSD" to 2345.50,
            "BTCUSD" to 68500.00,
            "DXY" to 104.30,
            "US30" to 39200.00
        )
    )
    val marketPrices: StateFlow<Map<String, Double>> = _marketPrices.asStateFlow()

    // Historical Prices for Real-Time Charts (glowing canvas)
    private val _chartHistory = MutableStateFlow<Map<String, List<Double>>>(
        mapOf(
            "EURUSD" to List(20) { 1.0810 + Random.nextDouble(0.0010, 0.0030) },
            "GBPUSD" to List(20) { 1.2720 + Random.nextDouble(0.0015, 0.0040) },
            "XAUUSD" to List(20) { 2335.00 + Random.nextDouble(5.0, 15.0) },
            "BTCUSD" to List(20) { 68100.00 + Random.nextDouble(100.0, 600.0) },
            "DXY" to List(20) { 104.10 + Random.nextDouble(0.1, 0.4) },
            "US30" to List(20) { 39100.00 + Random.nextDouble(50.0, 150.0) }
        )
    )
    val chartHistory: StateFlow<Map<String, List<Double>>> = _chartHistory.asStateFlow()

    // Active Simulated Open Trades floating PNL based on live pricing
    data class SimulatedPosition(
        val id: String,
        val pair: String,
        val isBuy: Boolean,
        val lots: Double,
        val entryPrice: Double,
        var currentPrice: Double,
        var floatingProfit: Double
    )

    private val _openPositions = MutableStateFlow<List<SimulatedPosition>>(emptyList())
    val openPositions: StateFlow<List<SimulatedPosition>> = _openPositions.asStateFlow()

    // Simulated Trade Copier Master log
    data class CopierLog(
        val timestamp: String,
        val sourceAccount: String,
        val targetAccount: String,
        val action: String, // "COPY BUY", "COPY SELL", "SYNC EQUITY"
        val status: String  // "EXECUTED", "ROUTED"
    )

    private val _copierLogs = MutableStateFlow<List<CopierLog>>(emptyList())
    val copierLogs: StateFlow<List<CopierLog>> = _copierLogs.asStateFlow()

    // AI Copilot State
    private val _selectedPair = MutableStateFlow("EURUSD")
    val selectedPair: StateFlow<String> = _selectedPair.asStateFlow()

    private val _selectedTimeframe = MutableStateFlow("H4")
    val selectedTimeframe: StateFlow<String> = _selectedTimeframe.asStateFlow()

    private val _selectedTheory = MutableStateFlow("Quarterly Theory + AMD")
    val selectedTheory: StateFlow<String> = _selectedTheory.asStateFlow()

    private val _geopoliticalInput = MutableStateFlow("Central bank speeches suggest minor hawkish rebalancing; DXY liquidity sweeping prior daily highs.")
    val geopoliticalInput: StateFlow<String> = _geopoliticalInput.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisReport = MutableStateFlow("")
    val analysisReport: StateFlow<String> = _analysisReport.asStateFlow()

    // Confluence Checker Toggles
    private val _emaAligned = MutableStateFlow(true)
    val emaAligned = _emaAligned.asStateFlow()

    private val _pivotBouncing = MutableStateFlow(true)
    val pivotBouncing = _pivotBouncing.asStateFlow()

    private val _rsiInZone = MutableStateFlow(true)
    val rsiInZone = _rsiInZone.asStateFlow()

    private val _bbExpanding = MutableStateFlow(true)
    val bbExpanding = _bbExpanding.asStateFlow()

    private val _htfBiasAligned = MutableStateFlow(true)
    val htfBiasAligned = _htfBiasAligned.asStateFlow()

    private val _liquiditySwept = MutableStateFlow(true)
    val liquiditySwept = _liquiditySwept.asStateFlow()

    private val _sessionKillZone = MutableStateFlow(true)
    val sessionKillZone = _sessionKillZone.asStateFlow()

    private val _m5StructureShift = MutableStateFlow(true)
    val m5StructureShift = _m5StructureShift.asStateFlow()

    private val _quarterlyQ3 = MutableStateFlow(true)
    val quarterlyQ3 = _quarterlyQ3.asStateFlow()

    // Coroutine Jobs for ticks and copiers
    private var priceTickJob: Job? = null

    init {
        // Observe database
        viewModelScope.launch {
            dao.getAllAccountsFlow().collectLatest { list ->
                if (list.isEmpty()) {
                    preseedAccounts()
                } else {
                    _accounts.value = list
                    // Set selected account if none selected
                    if (_selectedAccount.value == null) {
                        _selectedAccount.value = list.first()
                    }
                }
            }
        }

        // Start premium ticking live market feeds and equity drift
        startLiveTickingFeed()

        // Generate preliminary mock report on startup
        runMatrixAnalysis()
    }

    private fun preseedAccounts() {
        viewModelScope.launch {
            val seeds = listOf(
                Mt5Account(
                    id = "aizen_cap_1",
                    login = "10029584",
                    aliasName = "Aizen Sovereign Trust I",
                    broker = "IC Markets Elite",
                    accountGroup = "SMC Core Portfolio",
                    leverage = 200,
                    initialBalance = 10000000.0,
                    balance = 10345200.0,
                    equity = 10345200.0,
                    type = "INSTITUTIONAL"
                ),
                Mt5Account(
                    id = "macro_overlay_2",
                    login = "449204",
                    aliasName = "Global Macro Overlay",
                    broker = "Pepperstone Pro",
                    accountGroup = "SMC Macro Index",
                    leverage = 100,
                    initialBalance = 2000000.0,
                    balance = 2145800.0,
                    equity = 2145800.0,
                    type = "LIVE"
                ),
                Mt5Account(
                    id = "prop_tactical_3",
                    login = "889392",
                    aliasName = "XAU Tactical Prop Feed",
                    broker = "FTMO Private server",
                    accountGroup = "SMC Daily Scalp",
                    leverage = 100,
                    initialBalance = 200000.0,
                    balance = 214500.0,
                    equity = 214500.0,
                    type = "LIVE"
                )
            )
            for (seed in seeds) {
                dao.insertAccount(seed)
            }
        }
    }

    private fun startLiveTickingFeed() {
        priceTickJob?.cancel()
        priceTickJob = viewModelScope.launch {
            // Seed initial random floating positions
            _openPositions.value = listOf(
                SimulatedPosition("pos_1", "EURUSD", true, 20.0, 1.0815, 1.0825, 20002.0),
                SimulatedPosition("pos_2", "XAUUSD", true, 5.0, 2341.00, 2345.50, 2250.0)
            )

            while (true) {
                delay(1500) // Beautiful live refresh frequency
                
                // Tick market prices realistically
                val newPrices = _marketPrices.value.toMutableMap()
                val newHistory = _chartHistory.value.toMutableMap()

                newPrices.forEach { (symbol, currentPrice) ->
                    val deltaPercent = when (symbol) {
                        "BTCUSD" -> Random.nextDouble(-0.001, 0.001)
                        "XAUUSD" -> Random.nextDouble(-0.0006, 0.0006)
                        else -> Random.nextDouble(-0.0003, 0.0003)
                    }
                    val updatedPrice = currentPrice * (1.0 + deltaPercent)
                    newPrices[symbol] = updatedPrice

                    // Push to chart series log
                    val currentList = newHistory[symbol] ?: emptyList()
                    val newList = currentList.drop(1) + updatedPrice
                    newHistory[symbol] = newList
                }
                _marketPrices.value = newPrices
                _chartHistory.value = newHistory

                // Calculate updated positions profit & loss
                val positions = _openPositions.value.map { pos ->
                    val livePrice = newPrices[pos.pair] ?: pos.currentPrice
                    val priceDiff = if (pos.isBuy) (livePrice - pos.entryPrice) else (pos.entryPrice - livePrice)
                    
                    // Direct pip translation calculation
                    val factor = when(pos.pair) {
                        "XAUUSD" -> 100.0 // 1 point = $100 per lot
                        "BTCUSD" -> 10.0   // 1 point = $10 per lot
                        "US30" -> 10.0
                        else -> 100000.0  // 1 pip = $10 on standard FX lot (100,000 ratio)
                    }
                    val floatPnl = priceDiff * pos.lots * factor
                    pos.copy(currentPrice = livePrice, floatingProfit = floatPnl)
                }
                _openPositions.value = positions

                // Drift the Selected Account Equity & Balance dynamically to show incredible performance
                val currentAcc = _selectedAccount.value
                if (currentAcc != null) {
                    val totalFloating = positions.sumOf { it.floatingProfit }
                    val currentLiveEquity = currentAcc.balance + totalFloating
                    
                    val updatedAcc = currentAcc.copy(equity = currentLiveEquity)
                    _selectedAccount.value = updatedAcc

                    // Every 20 seconds, simulate copier action randomly
                    if (Random.nextInt(0, 15) == 5 && currentAcc.isCopierActive) {
                        triggerSimulatedCopierLogs(currentAcc)
                    }
                }
            }
        }
    }

    private fun triggerSimulatedCopierLogs(masterAcc: Mt5Account) {
        val targets = _accounts.value.filter { it.id != masterAcc.id }
        if (targets.isNotEmpty()) {
            val target = targets.random()
            val buySell = if (Random.nextBoolean()) "BUY" else "SELL"
            val symbol = listOf("EURUSD", "GBPUSD", "XAUUSD").random()
            val lots = (Random.nextInt(1, 5) * 0.5)

            val log = CopierLog(
                timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date()),
                sourceAccount = masterAcc.aliasName,
                targetAccount = target.aliasName,
                action = "COPY $buySell $lots Lots $symbol",
                status = "EXECUTED"
            )
            val currentLogs = _copierLogs.value
            _copierLogs.value = (listOf(log) + currentLogs).take(30)
        }
    }

    // Setters for AI Copilot Controls
    fun setPair(pair: String) {
        _selectedPair.value = pair
    }

    fun setTimeframe(tf: String) {
        _selectedTimeframe.value = tf
    }

    fun setTheory(th: String) {
        _selectedTheory.value = th
    }

    fun setGeopoliticalInput(text: String) {
        _geopoliticalInput.value = text
    }

    // Toggle Confluences
    fun toggleEma() { _emaAligned.value = !_emaAligned.value }
    fun togglePivot() { _pivotBouncing.value = !_pivotBouncing.value }
    fun toggleRsi() { _rsiInZone.value = !_rsiInZone.value }
    fun toggleBb() { _bbExpanding.value = !_bbExpanding.value }
    fun toggleHtf() { _htfBiasAligned.value = !_htfBiasAligned.value }
    fun toggleSweep() { _liquiditySwept.value = !_liquiditySwept.value }
    fun toggleKillZone() { _sessionKillZone.value = !_sessionKillZone.value }
    fun toggleM5Shift() { _m5StructureShift.value = !_m5StructureShift.value }
    fun toggleQuarter() { _quarterlyQ3.value = !_quarterlyQ3.value }

    fun getConfluenceScore(): Int {
        var score = 0
        if (emaAligned.value) score++
        if (pivotBouncing.value) score++
        if (rsiInZone.value) score++
        if (bbExpanding.value) score++
        if (htfBiasAligned.value) score++
        if (liquiditySwept.value) score++
        if (sessionKillZone.value) score++
        if (m5StructureShift.value) score++
        if (quarterlyQ3.value) score++
        return score
    }

    fun selectAccount(account: Mt5Account) {
        _selectedAccount.value = account
    }

    // Matrix Real Gemini API caller
    fun runMatrixAnalysis() {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisReport.value = "Establishing elite sovereign link... Contacting AizenEA matrix."
            
            val feedback = GeminiService.analyzeMarketStructure(
                pair = _selectedPair.value,
                timeframe = _selectedTimeframe.value,
                technicalContext = """
                    EMA 9/21/50 alignment: ${if (_emaAligned.value) "bullish perfect" else "mixed neutral"}, 
                    RSI 14 momentum: ${if (_rsiInZone.value) "45-65 bullish zone" else "oversold/neutral"}, 
                    Bollinger expansion: ${if (_bbExpanding.value) "expanding outward" else "contracting squeeze"}, 
                    Pivot daily correlation: ${if (_pivotBouncing.value) "interacting S1/S2" else "no confluence pivots"}
                """.trimIndent(),
                geopoliticalNews = _geopoliticalInput.value,
                theoryFocus = _selectedTheory.value
            )
            _analysisReport.value = feedback
            _isAnalyzing.value = false
        }
    }

    // CRUD MT5 accounts using Room Persistence Standardly
    fun addNewMt5Account(
        login: String,
        alias: String,
        broker: String,
        group: String,
        leverage: Int,
        balance: Double,
        accType: String
    ) {
        viewModelScope.launch {
            val account = Mt5Account(
                id = UUID.randomUUID().toString(),
                login = login,
                aliasName = alias,
                broker = broker,
                accountGroup = group,
                leverage = leverage,
                initialBalance = balance,
                balance = balance,
                equity = balance,
                type = accType,
                isCopierActive = true
            )
            dao.insertAccount(account)
            // Immediately focus on new
            _selectedAccount.value = account
        }
    }

    fun removeAccount(account: Mt5Account) {
        viewModelScope.launch {
            dao.deleteAccount(account)
            if (_selectedAccount.value?.id == account.id) {
                _selectedAccount.value = _accounts.value.firstOrNull { it.id != account.id }
            }
        }
    }

    fun toggleCopier(accountId: String, isActive: Boolean) {
        viewModelScope.launch {
            val current = _accounts.value.find { it.id == accountId }
            if (current != null) {
                val updated = current.copy(isCopierActive = isActive)
                dao.updateAccount(updated)
                if (_selectedAccount.value?.id == accountId) {
                    _selectedAccount.value = updated
                }
            }
        }
    }
}
