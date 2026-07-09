import React, { useState, useMemo, useEffect } from 'react'
import { residentService } from '../../services/resident/residentService'
import {
  LayoutDashboard,
  Users,
  ClipboardList,
  Pill,
  ShieldAlert,
  BarChart3,
  LogOut,
  Menu,
  Bell,
  HelpCircle,
  Search,
  ChevronDown,
  Table as TableIcon,
  Plus,
  UserCheck,
  UserX,
  Clock,
  X,
  ChevronRight,
  Info,
  Sun,
  Moon
} from 'lucide-react'

// Define the interface for a resident
interface Resident {
  id: string
  name: string
  room: string
  status: 'Active' | 'Pending' | 'Discharged'
  dob: string
  age: number
  payerSource: string
  referralSource: string
}

interface ResidentsListProps {
  onViewDetail?: (id: string) => void
}

export default function ResidentsList({ onViewDetail }: ResidentsListProps) {
  const [residents, setResidents] = useState<Resident[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('All')
  const [referralFilter, setReferralFilter] = useState<string>('All')
  const [selectedMenu, setSelectedMenu] = useState('Residents')
  
  // Theme state
  const [theme, setTheme] = useState<'light' | 'dark'>(() => {
    if (typeof window !== 'undefined') {
      const savedTheme = localStorage.getItem('theme')
      if (savedTheme === 'light' || savedTheme === 'dark') return savedTheme
      return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    }
    return 'light'
  })

  // Responsive mobile sidebar state
  const [isSidebarOpen, setIsSidebarOpen] = useState(false)

  // Modals / Drawers state
  const [isAddModalOpen, setIsAddModalOpen] = useState(false)
  const [selectedResident, setSelectedResident] = useState<Resident | null>(null)
  
  // New resident form state
  const [newResident, setNewResident] = useState<Omit<Resident, 'id' | 'age'>>({
    name: '',
    room: '',
    status: 'Active',
    dob: '',
    payerSource: 'Medicare',
    referralSource: 'Private'
  })

  // Dropdown menus open/close states
  const [isStatusDropdownOpen, setIsStatusDropdownOpen] = useState(false)
  const [isReferralDropdownOpen, setIsReferralDropdownOpen] = useState(false)

  // Handle Theme Effects
  useEffect(() => {
    const root = document.documentElement
    if (theme === 'dark') {
      root.classList.add('dark')
    } else {
      root.classList.remove('dark')
    }
    localStorage.setItem('theme', theme)
  }, [theme])

  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light')
  }

  const fetchResidents = async () => {
    try {
      const data = await residentService.getResidents(searchTerm, statusFilter, referralFilter)
      const mapped: Resident[] = data.map((r) => ({
        id: String(r.id),
        name: r.name,
        room: r.room,
        status: (r.status === 'Active' || r.status === 'Pending' || r.status === 'Discharged') ? r.status : 'Active',
        dob: r.dob,
        age: r.age,
        payerSource: r.payerSource,
        referralSource: r.referralSource
      }))
      setResidents(mapped)
    } catch (err) {
      console.error('Failed to fetch residents:', err)
    }
  }

  useEffect(() => {
    fetchResidents()
  }, [searchTerm, statusFilter, referralFilter])

  // Calculations for stats
  const stats = useMemo(() => {
    const total = residents.length
    const active = residents.filter((r) => r.status === 'Active').length
    const discharged = residents.filter((r) => r.status === 'Discharged').length
    const pending = residents.filter((r) => r.status === 'Pending').length
    return { total, active, discharged, pending }
  }, [residents])

  // Get unique referrals for filter options
  const referralSources = useMemo(() => {
    const refs = new Set(residents.map((r) => r.referralSource))
    return ['All', ...Array.from(refs)]
  }, [residents])

  // Filter residents based on search and filters (handled on backend)
  const filteredResidents = residents

  // Handle adding new resident
  const handleAddNewResident = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!newResident.name || !newResident.room || !newResident.dob) return

    const nameParts = newResident.name.trim().split(' ')
    const firstName = nameParts[0]
    const lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : 'Resident'

    try {
      await residentService.createResident({
        firstName,
        lastName,
        status: newResident.status,
        dob: newResident.dob,
        gender: 'Female',
        referralSource: newResident.referralSource,
        ssn: 'XXX-XX-8812',
        maritalStatus: 'Single',
        referringFacility: newResident.room,
        phone: '555-010-2213',
        address: '100 Main St, Riverside, CA',
        emergencyContact: 'Family Contact',
        emergencyPhone: '555-019-2234',
        poaOnFile: false,
        poaName: '',
        poaRelationship: '',
        payerSource: newResident.payerSource,
        payerType: 'Government',
        medicareNum: 'ENC-MCR-9921',
        insuranceProvider: newResident.payerSource,
        authStartDate: '',
        authEndDate: '',
        dnrActive: false
      })
      fetchResidents()
      
      // Reset form
      setNewResident({
        name: '',
        room: '',
        status: 'Active',
        dob: '',
        payerSource: 'Medicare',
        referralSource: 'Private'
      })
      setIsAddModalOpen(false)
    } catch (err) {
      console.error('Failed to create resident:', err)
      alert('Failed to add new resident')
    }
  }

  return (
    <div className="flex h-screen bg-[#f8fafc] dark:bg-slate-950 text-slate-800 dark:text-slate-100 font-sans overflow-hidden transition-colors duration-300">
      
      {/* MOBILE SIDEBAR OVERLAY */}
      {isSidebarOpen && (
        <div 
          className="fixed inset-0 bg-slate-900/40 dark:bg-slate-950/60 backdrop-blur-xs z-30 lg:hidden"
          onClick={() => setIsSidebarOpen(false)}
        />
      )}

      {/* SIDEBAR */}
      <aside className={`fixed inset-y-0 left-0 w-64 bg-white dark:bg-slate-900 border-r border-slate-200 dark:border-slate-800 flex flex-col justify-between z-40 transition-transform duration-300 transform lg:translate-x-0 lg:static lg:h-full ${
        isSidebarOpen ? 'translate-x-0' : '-translate-x-full'
      }`}>
        <div>
          {/* Logo Brand area */}
          <div className="p-5 flex items-center justify-between border-b border-slate-100 dark:border-slate-800 h-16">
            <div className="flex items-center gap-3">
              <span className="font-extrabold text-2xl text-blue-600 dark:text-blue-500 tracking-tight">NHMS</span>
              <div className="flex flex-col">
                <span className="text-[10px] text-slate-400 dark:text-slate-500 font-semibold uppercase leading-none">Nursing Home</span>
                <span className="text-[10px] text-slate-400 dark:text-slate-500 font-semibold uppercase leading-none">Management</span>
              </div>
            </div>
            {/* Mobile Close Button */}
            <button 
              className="lg:hidden p-1.5 rounded-lg text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800"
              onClick={() => setIsSidebarOpen(false)}
            >
              <X className="size-5" />
            </button>
          </div>

          {/* Navigation Links */}
          <nav className="p-4 space-y-1">
            <button
              onClick={() => { setSelectedMenu('Dashboard'); setIsSidebarOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all ${
                selectedMenu === 'Dashboard'
                  ? 'bg-blue-50 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 font-semibold shadow-xs'
                  : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100'
              }`}
            >
              <LayoutDashboard className="size-4" />
              Dashboard
            </button>
            <button
              onClick={() => { setSelectedMenu('Residents'); setIsSidebarOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all ${
                selectedMenu === 'Residents'
                  ? 'bg-blue-50 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 font-semibold shadow-xs'
                  : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100'
              }`}
            >
              <Users className="size-4" />
              Residents
            </button>
            <button
              onClick={() => { setSelectedMenu('Care Planning'); setIsSidebarOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all ${
                selectedMenu === 'Care Planning'
                  ? 'bg-blue-50 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 font-semibold shadow-xs'
                  : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100'
              }`}
            >
              <ClipboardList className="size-4" />
              Care Planning
            </button>
            
            {/* eMAR soon option */}
            <div className="w-full flex items-center justify-between px-3 py-2.5 rounded-lg text-sm font-medium text-slate-400 dark:text-slate-500 cursor-not-allowed">
              <div className="flex items-center gap-3">
                <Pill className="size-4" />
                <span>eMAR</span>
              </div>
              <span className="bg-slate-100 dark:bg-slate-800 text-slate-500 dark:text-slate-400 text-[10px] px-2 py-0.5 rounded-full font-bold uppercase tracking-wider scale-90">
                soon
              </span>
            </div>

            <button
              onClick={() => { setSelectedMenu('Incident & Risk'); setIsSidebarOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all ${
                selectedMenu === 'Incident & Risk'
                  ? 'bg-blue-50 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 font-semibold shadow-xs'
                  : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100'
              }`}
            >
              <ShieldAlert className="size-4" />
              Incident & Risk
            </button>
            <button
              onClick={() => { setSelectedMenu('Reports'); setIsSidebarOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all ${
                selectedMenu === 'Reports'
                  ? 'bg-blue-50 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 font-semibold shadow-xs'
                  : 'text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100'
              }`}
            >
              <BarChart3 className="size-4" />
              Reports
            </button>
          </nav>
        </div>

        {/* Logout at bottom */}
        <div className="p-4 border-t border-slate-100 dark:border-slate-800">
          <button className="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-rose-600 dark:text-rose-400 hover:bg-rose-50 dark:hover:bg-rose-950/20 transition-all">
            <LogOut className="size-4" />
            Logout
          </button>
        </div>
      </aside>

      {/* MAIN WRAPPER */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* TOP NAVBAR */}
        <header className="h-16 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between px-6 shrink-0 z-20">
          <div className="flex items-center gap-4">
            {/* Hamburger for mobile */}
            <button 
              className="text-slate-500 hover:text-slate-800 dark:text-slate-400 dark:hover:text-slate-100 lg:hidden p-1 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800"
              onClick={() => setIsSidebarOpen(true)}
            >
              <Menu className="size-5" />
            </button>
            <div className="hidden sm:flex flex-col">
              <span className="text-xs text-slate-400 dark:text-slate-500 font-medium">Nursing Home Management System</span>
            </div>
          </div>

          <div className="flex items-center gap-4">
            {/* Theme Toggle Button */}
            <button 
              onClick={toggleTheme}
              className="p-2 text-slate-400 dark:text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 rounded-full hover:bg-slate-50 dark:hover:bg-slate-800 transition-all"
              title={theme === 'light' ? 'Switch to Dark Mode' : 'Switch to Light Mode'}
            >
              {theme === 'light' ? <Moon className="size-5" /> : <Sun className="size-5" />}
            </button>

            {/* Notification */}
            <button className="relative p-2 text-slate-400 dark:text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 rounded-full hover:bg-slate-50 dark:hover:bg-slate-800 transition-all">
              <Bell className="size-5" />
              <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-rose-500 rounded-full"></span>
            </button>
            {/* Help */}
            <button className="p-2 text-slate-400 dark:text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 rounded-full hover:bg-slate-50 dark:hover:bg-slate-800 transition-all">
              <HelpCircle className="size-5" />
            </button>

            {/* Profile */}
            <div className="flex items-center gap-3 pl-2 border-l border-slate-200 dark:border-slate-800">
              <div className="flex flex-col text-right hidden sm:flex">
                <span className="text-sm font-semibold text-slate-800 dark:text-slate-200">Anna Lee</span>
                <span className="text-[11px] text-slate-400 dark:text-slate-500 font-medium leading-none">Nurse</span>
              </div>
              <div className="size-9 bg-blue-100 dark:bg-blue-950/80 text-blue-600 dark:text-blue-400 rounded-full flex items-center justify-center font-bold text-sm ring-2 ring-blue-50/50 dark:ring-blue-900/30">
                AL
              </div>
              <ChevronDown className="size-4 text-slate-400" />
            </div>
          </div>
        </header>

        {/* SCROLLABLE MAIN CONTENT */}
        <main className="flex-1 overflow-y-auto p-4 sm:p-6 space-y-6">
          {/* Breadcrumbs & Header */}
          <div className="flex flex-col gap-1">
            <div className="flex items-center gap-1.5 text-xs text-slate-400 dark:text-slate-500 font-medium">
              <span>Residents</span>
              <ChevronRight className="size-3 text-slate-300 dark:text-slate-700" />
              <span className="text-slate-600 dark:text-slate-300">List</span>
            </div>
            <h1 className="text-2xl font-bold text-slate-900 dark:text-white tracking-tight">Residents</h1>
            <p className="text-sm text-slate-400 dark:text-slate-500 font-medium">
              {filteredResidents.length} residents · sorted by Date Added (newest first)
            </p>
          </div>

          {/* Action and Filter Row */}
          <div className="flex flex-col xl:flex-row xl:items-center justify-between gap-3 bg-white dark:bg-slate-900 p-3 rounded-xl border border-slate-200/80 dark:border-slate-800 shadow-xs">
            {/* Search and Dropdowns */}
            <div className="flex flex-col sm:flex-row sm:flex-wrap items-stretch sm:items-center gap-2 flex-1">
              {/* Search Bar */}
              <div className="relative w-full sm:max-w-xs md:max-w-sm">
                <Search className="absolute left-3 top-2.5 size-4 text-slate-400 dark:text-slate-500" />
                <input
                  type="text"
                  placeholder="Search by name, room, or resident ID"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-lg pl-9 pr-4 py-2 text-sm text-slate-800 dark:text-slate-100 placeholder:text-slate-400 dark:placeholder:text-slate-500 focus:outline-hidden focus:border-blue-500 focus:bg-white dark:focus:bg-slate-900 transition-all"
                />
              </div>

              {/* Filters Container for inline wrapping */}
              <div className="flex flex-wrap items-center gap-2">
                {/* Status Filter */}
                <div className="relative flex-1 sm:flex-none">
                  <button
                    onClick={() => setIsStatusDropdownOpen(!isStatusDropdownOpen)}
                    onBlur={() => setTimeout(() => setIsStatusDropdownOpen(false), 200)}
                    className="w-full sm:w-auto flex items-center justify-between gap-2 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-2 text-sm font-medium text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800 min-w-[120px] text-left"
                  >
                    <span>Status: {statusFilter}</span>
                    <ChevronDown className="size-3.5 text-slate-400" />
                  </button>
                  {isStatusDropdownOpen && (
                    <div className="absolute left-0 mt-1.5 w-36 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-lg shadow-md z-30 py-1">
                      {['All', 'Active', 'Pending', 'Discharged'].map((status) => (
                        <button
                          key={status}
                          onClick={() => {
                            setStatusFilter(status)
                            setIsStatusDropdownOpen(false)
                          }}
                          className={`w-full text-left px-3 py-2 text-sm transition-all hover:bg-slate-50 dark:hover:bg-slate-800 ${
                            statusFilter === status ? 'text-blue-600 dark:text-blue-400 font-semibold bg-blue-50/50 dark:bg-blue-950/20' : 'text-slate-600 dark:text-slate-300'
                          }`}
                        >
                          {status}
                        </button>
                      ))}
                    </div>
                  )}
                </div>

                {/* Referral Filter */}
                <div className="relative flex-1 sm:flex-none">
                  <button
                    onClick={() => setIsReferralDropdownOpen(!isReferralDropdownOpen)}
                    onBlur={() => setTimeout(() => setIsReferralDropdownOpen(false), 200)}
                    className="w-full sm:w-auto flex items-center justify-between gap-2 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-2 text-sm font-medium text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800 min-w-[140px] text-left"
                  >
                    <span className="truncate">Referral: {referralFilter}</span>
                    <ChevronDown className="size-3.5 text-slate-400" />
                  </button>
                  {isReferralDropdownOpen && (
                    <div className="absolute left-0 mt-1.5 w-48 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-lg shadow-md z-30 py-1 max-h-56 overflow-y-auto">
                      {referralSources.map((source) => (
                        <button
                          key={source}
                          onClick={() => {
                            setReferralFilter(source)
                            setIsReferralDropdownOpen(false)
                          }}
                          className={`w-full text-left px-3 py-2 text-sm transition-all hover:bg-slate-50 dark:hover:bg-slate-800 truncate ${
                            referralFilter === source ? 'text-blue-600 dark:text-blue-400 font-semibold bg-blue-50/50 dark:bg-blue-950/20' : 'text-slate-600 dark:text-slate-300'
                          }`}
                        >
                          {source}
                        </button>
                      ))}
                    </div>
                  )}
                </div>

                {/* Layout Mode - Toggle style */}
                <button className="flex items-center justify-center gap-1.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-850 rounded-lg px-3 py-2 text-sm font-medium text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-750 transition-all">
                  <TableIcon className="size-4" />
                  <span>Table</span>
                </button>
              </div>
            </div>

            {/* Add New Button */}
            <button
              onClick={() => setIsAddModalOpen(true)}
              className="flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 dark:bg-blue-600 dark:hover:bg-blue-700 text-white rounded-lg px-4 py-2.5 text-sm font-semibold transition-all shadow-xs cursor-pointer active:scale-98"
            >
              <Plus className="size-4" />
              Add New Resident
            </button>
          </div>

          {/* STATS CARDS PANEL */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {/* Stat Card 1 - Total */}
            <div
              onClick={() => setStatusFilter('All')}
              className={`p-4 bg-white dark:bg-slate-900 rounded-xl border transition-all cursor-pointer select-none flex items-center justify-between group ${
                statusFilter === 'All'
                  ? 'border-blue-500 ring-2 ring-blue-50 dark:ring-blue-950/30 shadow-md'
                  : 'border-slate-200/80 dark:border-slate-800 hover:border-slate-300 dark:hover:border-slate-700 hover:shadow-xs'
              }`}
            >
              <div className="flex items-center gap-4">
                <div className="size-11 rounded-full bg-blue-50 dark:bg-blue-950/50 text-blue-600 dark:text-blue-400 flex items-center justify-center group-hover:scale-105 transition-all">
                  <Users className="size-5.5" />
                </div>
                <div className="flex flex-col">
                  <span className="text-xs font-semibold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Total Residents</span>
                  <span className="text-2xl font-bold text-slate-900 dark:text-white leading-tight">{stats.total}</span>
                </div>
              </div>
            </div>

            {/* Stat Card 2 - Active */}
            <div
              onClick={() => setStatusFilter('Active')}
              className={`p-4 bg-white dark:bg-slate-900 rounded-xl border transition-all cursor-pointer relative select-none flex items-center justify-between group ${
                statusFilter === 'Active'
                  ? 'border-emerald-500 ring-2 ring-emerald-50 dark:ring-emerald-950/30 shadow-md'
                  : 'border-slate-200/80 dark:border-slate-800 hover:border-slate-300 dark:hover:border-slate-700 hover:shadow-xs'
              }`}
            >
              <div className="flex items-center gap-4">
                <div className="size-11 rounded-full bg-emerald-50 dark:bg-emerald-950/50 text-emerald-600 dark:text-emerald-400 flex items-center justify-center group-hover:scale-105 transition-all">
                  <UserCheck className="size-5.5" />
                </div>
                <div className="flex flex-col">
                  <span className="text-xs font-semibold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Active</span>
                  <span className="text-2xl font-bold text-slate-900 dark:text-white leading-tight">{stats.active}</span>
                </div>
              </div>
              <div className="absolute top-3 right-3 text-emerald-500">
                <span className="size-4 border border-emerald-500/20 bg-emerald-50 dark:bg-emerald-950/30 rounded-full flex items-center justify-center text-[9px] font-bold">✓</span>
              </div>
            </div>

            {/* Stat Card 3 - Discharged */}
            <div
              onClick={() => setStatusFilter('Discharged')}
              className={`p-4 bg-white dark:bg-slate-900 rounded-xl border transition-all cursor-pointer select-none flex items-center justify-between group ${
                statusFilter === 'Discharged'
                  ? 'border-slate-500 dark:border-slate-400 ring-2 ring-slate-50 dark:ring-slate-900/30 shadow-md'
                  : 'border-slate-200/80 dark:border-slate-800 hover:border-slate-300 dark:hover:border-slate-700 hover:shadow-xs'
              }`}
            >
              <div className="flex items-center gap-4">
                <div className="size-11 rounded-full bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 flex items-center justify-center group-hover:scale-105 transition-all">
                  <UserX className="size-5.5" />
                </div>
                <div className="flex flex-col">
                  <span className="text-xs font-semibold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Discharged</span>
                  <span className="text-2xl font-bold text-slate-900 dark:text-white leading-tight">{stats.discharged}</span>
                </div>
              </div>
            </div>

            {/* Stat Card 4 - Pending */}
            <div
              onClick={() => setStatusFilter('Pending')}
              className={`p-4 bg-white dark:bg-slate-900 rounded-xl border transition-all cursor-pointer select-none flex items-center justify-between group ${
                statusFilter === 'Pending'
                  ? 'border-amber-500 ring-2 ring-amber-50 dark:ring-amber-950/30 shadow-md'
                  : 'border-slate-200/80 dark:border-slate-800 hover:border-slate-300 dark:hover:border-slate-700 hover:shadow-xs'
              }`}
            >
              <div className="flex items-center gap-4">
                <div className="size-11 rounded-full bg-amber-50 dark:bg-amber-950/50 text-amber-600 dark:text-amber-400 flex items-center justify-center group-hover:scale-105 transition-all">
                  <Clock className="size-5.5" />
                </div>
                <div className="flex flex-col">
                  <span className="text-xs font-semibold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Pending</span>
                  <span className="text-2xl font-bold text-slate-900 dark:text-white leading-tight">{stats.pending}</span>
                </div>
              </div>
            </div>
          </div>

          {/* TABLE CONTAINER */}
          <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 shadow-xs overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full border-collapse text-left text-sm min-w-[700px]">
                <thead>
                  <tr className="border-b border-slate-100 dark:border-slate-800 bg-slate-50 dark:bg-slate-850 text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">
                    <th className="px-6 py-4">Name</th>
                    <th className="px-6 py-4">Room</th>
                    <th className="px-6 py-4">Status</th>
                    <th className="px-6 py-4">DOB (Age)</th>
                    <th className="px-6 py-4">Payer Source</th>
                    <th className="px-6 py-4">Referral Source</th>
                    <th className="px-6 py-4 text-right"></th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 dark:divide-slate-800">
                  {filteredResidents.length > 0 ? (
                    filteredResidents.map((resident) => (
                      <tr
                        key={resident.id}
                        className="hover:bg-slate-50/70 dark:hover:bg-slate-800/40 transition-all group"
                      >
                        {/* Name */}
                        <td className="px-6 py-4 font-semibold text-slate-800 dark:text-slate-200">
                          {resident.name}
                        </td>
                        {/* Room */}
                        <td className="px-6 py-4 text-slate-600 dark:text-slate-300 font-medium">
                          {resident.room}
                        </td>
                        {/* Status Badge */}
                        <td className="px-6 py-4">
                          <span
                            className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${
                              resident.status === 'Active'
                                ? 'bg-emerald-50 dark:bg-emerald-950/30 text-emerald-700 dark:text-emerald-400 border border-emerald-100 dark:border-emerald-900/30'
                                : resident.status === 'Pending'
                                ? 'bg-amber-50 dark:bg-amber-950/30 text-amber-700 dark:text-amber-400 border border-amber-100 dark:border-amber-900/30'
                                : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-700'
                            }`}
                          >
                            {resident.status}
                          </span>
                        </td>
                        {/* DOB (Age) */}
                        <td className="px-6 py-4 text-slate-600 dark:text-slate-400">
                          {resident.dob} ({resident.age})
                        </td>
                        {/* Payer Source */}
                        <td className="px-6 py-4 text-slate-500 dark:text-slate-400 font-medium">
                          {resident.payerSource}
                        </td>
                        {/* Referral Source */}
                        <td className="px-6 py-4 text-slate-500 dark:text-slate-400 font-medium">
                          {resident.referralSource}
                        </td>
                        {/* Actions */}
                        <td className="px-6 py-4 text-right">
                          <button
                            onClick={() => {
                              if (onViewDetail) {
                                onViewDetail(resident.id)
                              } else {
                                setSelectedResident(resident)
                              }
                            }}
                            className="text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300 font-semibold hover:underline bg-transparent border-0 cursor-pointer text-xs"
                          >
                            View
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan={7} className="px-6 py-12 text-center text-slate-400 dark:text-slate-500">
                        <div className="flex flex-col items-center justify-center gap-2">
                          <Info className="size-8 text-slate-300 dark:text-slate-700" />
                          <span className="font-semibold text-sm">No residents found matching filters</span>
                        </div>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </main>
      </div>

      {/* VIEW RESIDENT DETAIL DRAWER/MODAL */}
      {selectedResident && (
        <div className="fixed inset-0 bg-slate-900/40 dark:bg-slate-950/60 backdrop-blur-xs flex justify-end z-50 animate-fade-in">
          <div className="bg-white dark:bg-slate-900 w-full max-w-md h-full shadow-2xl flex flex-col p-6 relative animate-slide-in-right">
            {/* Header */}
            <div className="flex items-center justify-between pb-4 border-b border-slate-100 dark:border-slate-800">
              <h2 className="text-xl font-bold text-slate-900 dark:text-white">Resident Details</h2>
              <button
                onClick={() => setSelectedResident(null)}
                className="p-1 rounded-full text-slate-400 dark:text-slate-500 hover:text-slate-600 dark:hover:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
              >
                <X className="size-5" />
              </button>
            </div>

            {/* Info Body */}
            <div className="flex-1 py-6 space-y-6 overflow-y-auto">
              {/* Profile Header */}
              <div className="flex items-center gap-4 bg-slate-50 dark:bg-slate-950/50 p-4 rounded-xl border border-slate-100 dark:border-slate-800">
                <div className="size-16 bg-blue-100 dark:bg-blue-950/80 text-blue-600 dark:text-blue-400 rounded-full flex items-center justify-center text-2xl font-bold">
                  {selectedResident.name.split(' ').map(n => n[0]).join('')}
                </div>
                <div>
                  <h3 className="font-bold text-lg text-slate-900 dark:text-white">{selectedResident.name}</h3>
                  <span className="text-sm text-slate-500 dark:text-slate-400 font-medium">Room {selectedResident.room}</span>
                </div>
              </div>

              {/* Data List */}
              <div className="space-y-4">
                <div className="grid grid-cols-2 py-2 border-b border-slate-100 dark:border-slate-800">
                  <span className="text-sm text-slate-400 dark:text-slate-500 font-medium">Resident ID</span>
                  <span className="text-sm text-slate-800 dark:text-slate-200 font-semibold text-right">{selectedResident.id}</span>
                </div>
                <div className="grid grid-cols-2 py-2 border-b border-slate-100 dark:border-slate-800">
                  <span className="text-sm text-slate-400 dark:text-slate-500 font-medium">Status</span>
                  <span className="text-right">
                    <span className={`inline-flex px-2.5 py-0.5 rounded-full text-xs font-semibold ${
                      selectedResident.status === 'Active' ? 'bg-emerald-50 dark:bg-emerald-950/30 text-emerald-700 dark:text-emerald-400' : 'bg-amber-50 dark:bg-amber-950/30 text-amber-700 dark:text-amber-400'
                    }`}>
                      {selectedResident.status}
                    </span>
                  </span>
                </div>
                <div className="grid grid-cols-2 py-2 border-b border-slate-100 dark:border-slate-800">
                  <span className="text-sm text-slate-400 dark:text-slate-500 font-medium">Date of Birth</span>
                  <span className="text-sm text-slate-800 dark:text-slate-200 font-semibold text-right">{selectedResident.dob}</span>
                </div>
                <div className="grid grid-cols-2 py-2 border-b border-slate-100 dark:border-slate-800">
                  <span className="text-sm text-slate-400 dark:text-slate-500 font-medium">Age</span>
                  <span className="text-sm text-slate-800 dark:text-slate-200 font-semibold text-right">{selectedResident.age} years old</span>
                </div>
                <div className="grid grid-cols-2 py-2 border-b border-slate-100 dark:border-slate-800">
                  <span className="text-sm text-slate-400 dark:text-slate-500 font-medium">Payer Source</span>
                  <span className="text-sm text-slate-800 dark:text-slate-200 font-semibold text-right">{selectedResident.payerSource}</span>
                </div>
                <div className="grid grid-cols-2 py-2 border-b border-slate-100 dark:border-slate-800">
                  <span className="text-sm text-slate-400 dark:text-slate-500 font-medium">Referral Source</span>
                  <span className="text-sm text-slate-800 dark:text-slate-200 font-semibold text-right">{selectedResident.referralSource}</span>
                </div>
              </div>
            </div>

            {/* Bottom Actions */}
            <div className="pt-4 border-t border-slate-100 dark:border-slate-800 flex gap-2">
              <button
                onClick={() => setSelectedResident(null)}
                className="flex-1 py-2 text-center text-sm font-semibold border border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800 text-slate-600 dark:text-slate-400 rounded-lg"
              >
                Close
              </button>
              <button
                onClick={() => {
                  setResidents(residents.filter(r => r.id !== selectedResident.id))
                  setSelectedResident(null)
                }}
                className="flex-1 py-2 text-center text-sm font-semibold bg-rose-50 dark:bg-rose-950/20 hover:bg-rose-100 dark:hover:bg-rose-900/40 text-rose-600 dark:text-rose-450 rounded-lg transition-all"
              >
                Discharge Resident
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ADD RESIDENT MODAL */}
      {isAddModalOpen && (
        <div className="fixed inset-0 bg-slate-900/40 dark:bg-slate-950/60 backdrop-blur-xs flex items-center justify-center z-50 p-4 animate-fade-in">
          <form
            onSubmit={handleAddNewResident}
            className="bg-white dark:bg-slate-900 rounded-xl shadow-2xl border border-slate-100 dark:border-slate-800 w-full max-w-md p-6 space-y-4 animate-scale-up"
          >
            {/* Header */}
            <div className="flex items-center justify-between pb-3 border-b border-slate-100 dark:border-slate-800">
              <h2 className="text-lg font-bold text-slate-950 dark:text-white">Add New Resident</h2>
              <button
                type="button"
                onClick={() => setIsAddModalOpen(false)}
                className="p-1 rounded-full text-slate-400 dark:text-slate-500 hover:text-slate-600 dark:hover:text-slate-350 hover:bg-slate-100 dark:hover:bg-slate-800"
              >
                <X className="size-5" />
              </button>
            </div>

            {/* Content Form fields */}
            <div className="space-y-3">
              <div>
                <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider mb-1">Full Name</label>
                <input
                  required
                  type="text"
                  placeholder="e.g. John Doe"
                  value={newResident.name}
                  onChange={(e) => setNewResident({ ...newResident, name: e.target.value })}
                  className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-2 text-sm text-slate-800 dark:text-slate-100 focus:outline-hidden focus:border-blue-500 focus:bg-white dark:focus:bg-slate-900"
                />
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider mb-1">Room Number</label>
                  <input
                    required
                    type="text"
                    placeholder="e.g. 102-B"
                    value={newResident.room}
                    onChange={(e) => setNewResident({ ...newResident, room: e.target.value })}
                    className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-2 text-sm text-slate-800 dark:text-slate-100 focus:outline-hidden focus:border-blue-500 focus:bg-white dark:focus:bg-slate-900"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider mb-1">Date of Birth</label>
                  <input
                    required
                    type="date"
                    value={newResident.dob}
                    onChange={(e) => setNewResident({ ...newResident, dob: e.target.value })}
                    className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-2 text-sm text-slate-800 dark:text-slate-100 focus:outline-hidden focus:border-blue-500 focus:bg-white dark:focus:bg-slate-900"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider mb-1">Status</label>
                  <select
                    value={newResident.status}
                    onChange={(e) => setNewResident({ ...newResident, status: e.target.value as 'Active' | 'Pending' })}
                    className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-1.5 text-sm text-slate-800 dark:text-slate-100 focus:outline-hidden"
                  >
                    <option value="Active">Active</option>
                    <option value="Pending">Pending</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider mb-1">Payer Source</label>
                  <input
                    type="text"
                    placeholder="e.g. Medicare"
                    value={newResident.payerSource}
                    onChange={(e) => setNewResident({ ...newResident, payerSource: e.target.value })}
                    className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-2 text-sm text-slate-800 dark:text-slate-100 focus:outline-hidden focus:border-blue-500 focus:bg-white dark:focus:bg-slate-900"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider mb-1">Referral Source</label>
                <input
                  type="text"
                  placeholder="e.g. Private, Family, Hospital Name"
                  value={newResident.referralSource}
                  onChange={(e) => setNewResident({ ...newResident, referralSource: e.target.value })}
                  className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-lg px-3 py-2 text-sm text-slate-800 dark:text-slate-100 focus:outline-hidden focus:border-blue-500 focus:bg-white dark:focus:bg-slate-900"
                />
              </div>
            </div>

            {/* Actions */}
            <div className="pt-3 border-t border-slate-100 dark:border-slate-800 flex justify-end gap-2">
              <button
                type="button"
                onClick={() => setIsAddModalOpen(false)}
                className="px-4 py-2 border border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800 rounded-lg text-sm font-semibold text-slate-600 dark:text-slate-400 transition-all"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-sm font-semibold transition-all shadow-xs"
              >
                Save Resident
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  )
}
