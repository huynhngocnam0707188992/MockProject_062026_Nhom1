import { useState, useEffect } from 'react'
import { residentService } from '../../services/resident/residentService'
import type { ResidentDetail as ApiResidentDetail } from '../../services/resident/residentService'
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
  ChevronDown,
  ChevronRight,
  Sun,
  Moon,
  Edit,
  AlertTriangle,
  Plus,
  ArrowLeft,
  X
} from 'lucide-react'

interface ResidentDetailProps {
  residentId: string
  onBack: () => void
  onEdit?: () => void
}

export default function ResidentDetail({ residentId, onBack, onEdit }: ResidentDetailProps) {
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
  const [activeTab, setActiveTab] = useState('Overview')

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

  const [profile, setProfile] = useState<ApiResidentDetail | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchDetail = async () => {
      try {
        setLoading(true)
        const data = await residentService.getResidentDetail(residentId)
        setProfile(data)
      } catch (err) {
        console.error('Failed to load resident details:', err)
      } finally {
        setLoading(false)
      }
    }
    if (residentId) {
      fetchDetail()
    }
  }, [residentId])

  if (loading || !profile) {
    return (
      <div className="flex h-screen bg-[#f8fafc] dark:bg-slate-950 text-slate-800 dark:text-slate-100 items-center justify-center">
        <span className="font-semibold text-lg text-slate-400">Loading resident details...</span>
      </div>
    )
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
              onClick={() => { onBack(); setIsSidebarOpen(false); }}
              className="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100 transition-all"
            >
              <LayoutDashboard className="size-4" />
              Dashboard
            </button>
            <button
              onClick={() => { onBack(); setIsSidebarOpen(false); }}
              className="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-semibold bg-blue-50 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 shadow-xs"
            >
              <Users className="size-4" />
              Residents
            </button>
            <button
              onClick={() => setIsSidebarOpen(false)}
              className="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100 transition-all"
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
              onClick={() => setIsSidebarOpen(false)}
              className="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100 transition-all"
            >
              <ShieldAlert className="size-4" />
              Incident & Risk
            </button>
            <button
              onClick={() => setIsSidebarOpen(false)}
              className="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-800 dark:hover:text-slate-100 transition-all"
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
          {/* Breadcrumbs */}
          <div className="flex items-center gap-1.5 text-xs text-slate-400 dark:text-slate-500 font-medium">
            <button onClick={onBack} className="hover:text-blue-600 dark:hover:text-blue-400 transition-all flex items-center gap-1">
              <ArrowLeft className="size-3" />
              <span>Residents</span>
            </button>
            <ChevronRight className="size-3 text-slate-300 dark:text-slate-700" />
            <span className="text-slate-600 dark:text-slate-300">{profile.name}</span>
          </div>

          {/* Profile Header Block */}
          <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-xs flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="flex items-start sm:items-center gap-4">
              {/* Initials Avatar */}
              <div className="size-16 sm:size-20 bg-slate-100 dark:bg-slate-850 text-slate-500 dark:text-slate-400 rounded-xl border border-slate-200 dark:border-slate-800 flex items-center justify-center text-xl sm:text-2xl font-bold shrink-0 shadow-xs">
                {profile.initials}
              </div>
              <div className="space-y-1">
                <h1 className="text-2xl sm:text-3xl font-extrabold text-slate-900 dark:text-white tracking-tight">{profile.name}</h1>
                <p className="text-xs sm:text-sm text-slate-400 dark:text-slate-500 font-semibold">
                  DOB {profile.dob} &middot; Room {profile.room} &middot; Resident ID {profile.id}
                </p>
                {/* Badges Row */}
                <div className="flex flex-wrap gap-1.5 pt-1.5">
                  {profile.badges.map((b) => (
                    <span
                      key={b.text}
                      className={`inline-flex px-2.5 py-0.5 rounded-full text-xs font-semibold border ${
                        b.type === 'active'
                          ? 'bg-emerald-50 dark:bg-emerald-950/20 text-emerald-700 dark:text-emerald-400 border-emerald-100 dark:border-emerald-900/30'
                          : b.type === 'nodnr'
                          ? 'bg-emerald-50 dark:bg-emerald-950/20 text-emerald-700 dark:text-emerald-400 border-emerald-100 dark:border-emerald-900/30'
                          : b.type === 'level3'
                          ? 'bg-amber-50 dark:bg-amber-950/20 text-amber-700 dark:text-amber-400 border-amber-100 dark:border-amber-900/30'
                          : 'bg-slate-50 dark:bg-slate-800 text-slate-500 dark:text-slate-400 border-slate-200 dark:border-slate-700'
                      }`}
                    >
                      {b.text}
                    </span>
                  ))}
                </div>
              </div>
            </div>

            {/* Actions Buttons */}
            <div className="flex flex-wrap items-center gap-2">
              <button 
                onClick={onEdit}
                className="flex items-center justify-center gap-1.5 bg-white dark:bg-slate-900 hover:bg-slate-50 dark:hover:bg-slate-800 border border-slate-250 dark:border-slate-700 rounded-lg px-3 py-2 text-sm font-semibold text-slate-700 dark:text-slate-300 transition-all cursor-pointer"
              >
                <Edit className="size-4" />
                Edit Profile
              </button>
              <button className="flex items-center justify-center gap-1.5 bg-white dark:bg-slate-900 hover:bg-slate-50 dark:hover:bg-slate-800 border border-slate-250 dark:border-slate-700 rounded-lg px-3 py-2 text-sm font-semibold text-slate-700 dark:text-slate-300 transition-all cursor-pointer">
                <AlertTriangle className="size-4 text-slate-400 dark:text-slate-500" />
                Report Incident
              </button>
              <button className="flex items-center justify-center gap-1.5 bg-blue-600 hover:bg-blue-700 text-white rounded-lg px-4 py-2.5 text-sm font-semibold transition-all shadow-xs cursor-pointer">
                <Plus className="size-4" />
                New Assessment
              </button>
            </div>
          </div>

          {/* Sub Navigation Tabs */}
          <div className="border-b border-slate-200 dark:border-slate-800">
            <div className="flex flex-wrap -mb-px gap-6 overflow-x-auto">
              {['Overview', 'Assessments', 'Care Plan', 'eMAR', 'LOC', 'Audit Log'].map((tab) => (
                <button
                  key={tab}
                  onClick={() => setActiveTab(tab)}
                  className={`py-3.5 border-b-2 font-semibold text-sm transition-all whitespace-nowrap cursor-pointer ${
                    activeTab === tab
                      ? 'border-blue-600 dark:border-blue-500 text-blue-600 dark:text-blue-400'
                      : 'border-transparent text-slate-400 dark:text-slate-500 hover:text-slate-700 dark:hover:text-slate-300'
                  }`}
                >
                  {tab}
                </button>
              ))}
            </div>
          </div>

          {/* TAB CONTENT */}
          {activeTab === 'Overview' && (
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 items-start">
              
              {/* LEFT & CENTER PANEL (2/3 width on desktop) */}
              <div className="lg:col-span-2 space-y-6">
                
                {/* Demographics Card */}
                <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 shadow-xs overflow-hidden">
                  <div className="px-6 py-4 border-b border-slate-100 dark:border-slate-800 flex items-center justify-between">
                    <h2 className="text-base font-bold text-slate-900 dark:text-white">Demographics</h2>
                    <span className="text-xs text-slate-400 dark:text-slate-500 font-medium">Role: Nurse — view + edit clinical fields</span>
                  </div>
                  <div className="p-6 grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-4">
                    {/* Legal Name */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Legal Name</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.legalName}</p>
                    </div>
                    {/* Address */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Address</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.address}</p>
                    </div>
                    {/* Date of Birth */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Date of Birth</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.dob}</p>
                    </div>
                    {/* Admission Date */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Admission Date</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.admissionDate}</p>
                    </div>
                    {/* SSN */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">SSN</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.ssn}</p>
                    </div>
                    {/* Room / Bed */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Room / Bed</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.roomBed}</p>
                    </div>
                    {/* Gender */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Gender</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.gender}</p>
                    </div>
                    {/* Referral Source */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Referral Source</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.referralSource}</p>
                    </div>
                    {/* Marital Status */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Marital Status</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.maritalStatus}</p>
                    </div>
                    {/* Emergency Contact */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Emergency Contact</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.emergencyContact}</p>
                    </div>
                    {/* Phone */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Phone</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.phone}</p>
                    </div>
                    {/* Payer Source */}
                    <div className="space-y-0.5">
                      <span className="text-[11px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">Payer Source</span>
                      <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{profile.demographics.payerSource}</p>
                    </div>
                  </div>
                </div>

                {/* Authorized Representative / POA Card */}
                <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 shadow-xs overflow-hidden">
                  <div className="px-6 py-4 border-b border-slate-100 dark:border-slate-800">
                    <h2 className="text-base font-bold text-slate-900 dark:text-white">Authorized Representative / POA</h2>
                  </div>
                  <div className="overflow-x-auto">
                    <table className="w-full text-left text-sm">
                      <thead>
                        <tr className="border-b border-slate-100 dark:border-slate-800 bg-slate-50 dark:bg-slate-850 text-[10px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-wider">
                          <th className="px-6 py-3">Name</th>
                          <th className="px-6 py-3">Relationship</th>
                          <th className="px-6 py-3">Contact</th>
                          <th className="px-6 py-3">DNR Flag</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr className="text-sm">
                          <td className="px-6 py-4 font-semibold text-slate-800 dark:text-slate-200">{profile.poa.name}</td>
                          <td className="px-6 py-4 text-slate-600 dark:text-slate-350">{profile.poa.relationship}</td>
                          <td className="px-6 py-4 text-slate-600 dark:text-slate-350 font-medium">{profile.poa.contact}</td>
                          <td className="px-6 py-4 text-slate-600 dark:text-slate-350">{profile.poa.dnrFlag}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>

              </div>

              {/* RIGHT CLINICAL/INSURANCE PANEL (1/3 width on desktop) */}
              <div className="space-y-6">
                
                {/* Diagnosis (ICD-10) Card */}
                <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-xs space-y-3">
                  <h2 className="text-base font-bold text-slate-950 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">Diagnosis (ICD-10)</h2>
                  <ul className="space-y-2 text-sm text-slate-700 dark:text-slate-300 font-medium list-disc list-inside">
                    {profile.diagnoses.map((diag) => (
                      <li key={diag} className="hover:text-blue-600 dark:hover:text-blue-400 transition-colors">{diag}</li>
                    ))}
                  </ul>
                </div>

                {/* Allergies Card */}
                <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-xs space-y-3">
                  <h2 className="text-base font-bold text-slate-950 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">Allergies</h2>
                  <div className="flex flex-wrap gap-2 pt-1">
                    {profile.allergies.map((alg) => (
                      <span
                        key={alg}
                        className="inline-flex px-3 py-1 bg-red-50 dark:bg-red-950/20 text-red-600 dark:text-red-400 border border-red-100/50 dark:border-red-900/30 text-xs font-semibold rounded-md transition-all hover:scale-105"
                      >
                        {alg}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Insurance / Payer Card */}
                <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-xs space-y-4">
                  <h2 className="text-base font-bold text-slate-950 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">Insurance / Payer</h2>
                  
                  <div className="space-y-3 text-sm">
                    <div className="flex justify-between items-start gap-4">
                      <span className="text-slate-400 dark:text-slate-500 font-medium text-xs">Medicare Number</span>
                      <span className="font-semibold text-slate-800 dark:text-slate-200 text-right">{profile.insurance.medicareNum}</span>
                    </div>
                    <div className="flex justify-between items-start gap-4">
                      <span className="text-slate-400 dark:text-slate-500 font-medium text-xs">Insurance Provider</span>
                      <span className="font-semibold text-slate-800 dark:text-slate-200 text-right">{profile.insurance.provider}</span>
                    </div>
                    <div className="flex justify-between items-start gap-4">
                      <span className="text-slate-400 dark:text-slate-500 font-medium text-xs">Payer Name</span>
                      <span className="font-semibold text-slate-800 dark:text-slate-200 text-right">{profile.insurance.payerName}</span>
                    </div>
                    <div className="flex justify-between items-start gap-4">
                      <span className="text-slate-400 dark:text-slate-500 font-medium text-xs">Auth Number</span>
                      <span className="font-semibold text-slate-800 dark:text-slate-200 text-right">{profile.insurance.authNum}</span>
                    </div>
                    <div className="flex justify-between items-start gap-4">
                      <span className="text-slate-400 dark:text-slate-500 font-medium text-xs">Auth Start / End</span>
                      <span className="font-semibold text-slate-800 dark:text-slate-200 text-right">{profile.insurance.authStartEnd}</span>
                    </div>
                  </div>
                </div>

                {/* LOC Summary Card */}
                <div className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-xs space-y-4">
                  <h2 className="text-base font-bold text-slate-950 dark:text-white border-b border-slate-100 dark:border-slate-800 pb-2">LOC Summary</h2>
                  <div className="space-y-3">
                    <div>
                      <span className="inline-flex px-2.5 py-0.5 bg-amber-50 dark:bg-amber-950/20 text-amber-700 dark:text-amber-400 border border-amber-100 dark:border-amber-900/30 text-xs font-semibold rounded-full">
                        {profile.locSummary.level}
                      </span>
                    </div>
                    <p className="text-sm font-semibold text-slate-850 dark:text-slate-200">
                      ADL Score: <span className="text-slate-900 dark:text-white">{profile.locSummary.adlScore}</span>
                    </p>
                    <a
                      href="#loc-history"
                      className="inline-block text-xs font-bold text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300 hover:underline"
                    >
                      View LOC History &rarr;
                    </a>
                  </div>
                </div>

              </div>

            </div>
          )}
        </main>
      </div>

    </div>
  )
}
