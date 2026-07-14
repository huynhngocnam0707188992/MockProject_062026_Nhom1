interface Props {
    levelCode: string;
}

export default function LocSummaryCard({ levelCode }: Props) {
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

            <div className="bg-white rounded-2xl border border-gray-200 p-6">

                <p className="text-sm text-gray-500 mb-2">
                    ADL Score
                </p>

                <h2 className="text-6xl font-bold">
                    20
                    <span className="text-3xl text-gray-400">
                        {" "} / 32
                    </span>
                </h2>

            </div>

            <div className="md:col-span-2 bg-gradient-to-r from-yellow-50 to-amber-50 border border-yellow-200 rounded-2xl p-6">

                <p className="uppercase tracking-widest text-xs text-amber-600">
                    Suggested LOC
                </p>

                <h2 className="text-3xl font-bold text-amber-700 mt-2">
                    {levelCode} — Extensive Assistance
                </h2>

            </div>

        </div>
    );
}